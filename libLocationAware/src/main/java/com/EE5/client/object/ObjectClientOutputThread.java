package com.EE5.client.object;

import android.util.Log;

import com.EE5.client.AbstractClientOutputThread;
import com.EE5.client.Client;
import com.EE5.client.LatencyTest;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ObjectClientOutputThread extends AbstractClientOutputThread {
    private ObjectOutputStream oos;

    public ObjectClientOutputThread(OutputStream out, Client client)throws IOException {
        super(out, client);
        oos = new ObjectOutputStream(out);
    }

    @Override
    public void run() {
        try {
            this.transmit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void transmit()throws IOException, InterruptedException{
        Log.i("Connection", "[ OK ] Sending positions");
        while (keepRunning()) {
            if (this.getDevice() != null) {
                LatencyTest.startTime = System.nanoTime();
                oos.reset();
                //Very Important!!!!!!!!!!
                //ObjectOutputSteam keeps some sort of cache,
                // the result is that when writing the same (updated) object multiple times,
                // the old version is used.
                oos.writeObject(this.getDevice());
                oos.writeObject(this.getCustomData());
                oos.flush();

                synchronized (this) {
                    //Wait until new message has arrived.
                    wait();
                }
            }
        }

        Log.i("Connection", "[ - ] Closing Connection");
        //Write NULL to server to signal end of transmission.
        oos.reset();
        oos.writeObject(null);
        oos.flush();
        oos.close();

        Log.i("Connection", "[ OK ] Outputstream closed.");
    }
}