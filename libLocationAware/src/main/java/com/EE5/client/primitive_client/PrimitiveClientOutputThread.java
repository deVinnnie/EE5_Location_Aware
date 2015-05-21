package com.EE5.client.primitive_client;

import android.util.Log;

import com.EE5.client.AbstractClientOutputThread;
import com.EE5.client.Client;
import com.EE5.client.LatencyTest;
import com.EE5.util.GlobalResources;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PrimitiveClientOutputThread extends AbstractClientOutputThread{
    private DataOutputStream od;

    public PrimitiveClientOutputThread(OutputStream out, Client client) {
        super(out, client);
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
        od = new DataOutputStream(this.getOutputStream());
        Log.i("Connection", "[ OK ] Sending positions");
        String id = GlobalResources.getInstance().getDevice().getId();
        od.writeUTF(id);

        synchronized (this) {
            //Wait for first "message" to arrive.
            wait();
        }
        while (keepRunning()) {
            if (this.getDevice() != null) {
                LatencyTest.startTime = System.nanoTime();

                od.writeDouble(this.getDevice().getPosition().getX());
                od.writeDouble(this.getDevice().getPosition().getY());
                od.writeDouble(this.getDevice().getPosition().getHeight());
                od.writeDouble(this.getDevice().getPosition().getRotation());
                String data = GlobalResources.getInstance().getData();
                od.writeUTF(data);

                od.flush();

                synchronized (this) {
                    //Wait until new message has arrived.
                    wait();
                }
            }
        }

        Log.i("Connection", "[ - ] Closing Connection");
        od.close();
        Log.i("Connection", "[ OK ] Outputstream closed.");
    }
}