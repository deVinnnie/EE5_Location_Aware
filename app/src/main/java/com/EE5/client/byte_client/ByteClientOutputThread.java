package com.EE5.client.byte_client;

import android.util.Log;

import com.EE5.client.AbstractClientOutputThread;
import com.EE5.client.Client;
import com.EE5.client.LatencyTest;
import com.EE5.client.Transmitter;
import com.EE5.server.data.Device;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ByteClientOutputThread extends AbstractClientOutputThread implements Transmitter{
    private Device device;
    private DataOutputStream od;

    /*public ByteClientOutputThread(Socket socket, Client client)throws IOException {
        super("OutputThread", socket, client);
        od = new DataOutputStream(socket.getOutputStream());
    }*/

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
            if (this.device != null) {
                LatencyTest.startTime = System.nanoTime();
                od.writeDouble(this.device.getPosition().getX());
                od.writeDouble(this.device.getPosition().getY());
                od.writeDouble(this.device.getPosition().getRotation());
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

    public void setDevice(Device device) {
        this.device = device;
        synchronized (this) {
            //Notify that new message has arrived.
            notify();
        }
    }
}