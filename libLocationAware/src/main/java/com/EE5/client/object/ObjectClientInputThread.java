package com.EE5.client.object;

import android.util.Log;

import com.EE5.client.AbstractClientInputThread;
import com.EE5.client.Client;
import com.EE5.client.LatencyTest;
import com.EE5.server.data.DeviceList;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class ObjectClientInputThread extends AbstractClientInputThread {
    private ObjectInputStream ois;

    public ObjectClientInputThread(InputStream in, Client client) throws IOException {
        super(in, client);
        this.ois = new ObjectInputStream(in);
        //Note: ObjectInputStream blocks until it has a connection.
    }

    @Override
    public void run() {
        Log.i("Connection", "[ OK ] Started Listening");
        try {
            while (keepRunning()) {
                DeviceList deviceList = (DeviceList) ois.readObject();
                //String deviceIds = "\n";
                long timeDif = LatencyTest.getLatency();
                Log.i("Timing", "" + timeDif + "ms");
                getClient().update("" + timeDif + "ms");
            }
        }
        catch(EOFException ex){
        }
        catch(Exception ex){
        }

        try {
            //Clean up.
            ois.close();
            Log.i("Connection", "[ OK ] InputStream Closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
