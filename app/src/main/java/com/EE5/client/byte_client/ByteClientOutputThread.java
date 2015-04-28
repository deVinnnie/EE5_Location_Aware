package com.EE5.client.byte_client;

import android.util.Log;

import com.EE5.client.AbstractClientOutputThread;
import com.EE5.client.Client;
import com.EE5.client.LatencyTest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ByteClientOutputThread extends AbstractClientOutputThread{
    private DataOutputStream od;

    public ByteClientOutputThread(OutputStream out, Client client) {
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
        od.writeUTF(Client.currentDevice.getId());

        synchronized (this) {
            //Wait until new message has arrived.
            wait();
        }
        while (keepRunning()) {
            if (this.getDevice() != null) {
                LatencyTest.startTime = System.nanoTime();

                /*od.writeDouble(this.getDevice().getPosition().getX());
                od.writeDouble(this.getDevice().getPosition().getY());
                od.writeDouble(this.getDevice().getPosition().getRotation());*/
                od.writeDouble(this.getDevice().getPattern().getNum1().x);
                od.writeDouble(this.getDevice().getPattern().getNum1().y);

                od.writeDouble(this.getDevice().getPattern().getNum2().x);
                od.writeDouble(this.getDevice().getPattern().getNum2().y);

                od.writeDouble(this.getDevice().getPattern().getNum3().x);
                od.writeDouble(this.getDevice().getPattern().getNum3().y);

                od.writeDouble(this.getDevice().getPattern().getNum4().x);
                od.writeDouble(this.getDevice().getPattern().getNum4().y);

                od.writeDouble(this.getDevice().getPattern().getAngle());

                od.flush();

                synchronized (this) {
                    //Wait until new message has arrived.
                    wait();
                }
            }
        }

        Log.i("Connection", "[ - ] Closing Connection");
        //Write NULL to server to signal end of transmission.
        /*oos.reset();
        oos.writeObject(null);
        oos.flush();
        oos.close();*/

        Log.i("Connection", "[ OK ] Outputstream closed.");
    }
}