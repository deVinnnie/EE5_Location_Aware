package com.EE5.client.primitive_client;

import android.util.Log;

import com.EE5.client.AbstractClientInputThread;
import com.EE5.client.Client;
import com.EE5.client.LatencyTest;
import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class PrimitiveClientInputThread extends AbstractClientInputThread {
    private DataInputStream id;

    public PrimitiveClientInputThread(InputStream in, Client client) {
        super(in, client);
    }

    @Override
    public void run() {
        id = new DataInputStream(this.getInputStream());
        Log.i("Connection", "[ OK ] Started Listening");

        try {
            while (keepRunning()) {
                boolean reachedEnd= false;
                while(!reachedEnd) {
                    String device = id.readUTF();
                    if(device.equals("000")){
                        reachedEnd = true;
                    }
                    else {
                        Log.i("Device", device);
                        double x = id.readDouble();
                        double y = id.readDouble();
                        double z = id.readDouble();
                        double rotation = id.readDouble();

                        /*double x1 = id.readDouble();
                        double y1 = id.readDouble();

                        double x2 = id.readDouble();
                        double y2 = id.readDouble();

                        double x3 = id.readDouble();
                        double y3 = id.readDouble();

                        double x4 = id.readDouble();
                        double y4 = id.readDouble();

                        double angle = id.readDouble();

                        PatternCoordinator pc = new PatternCoordinator(
                                new Point(x1,y1),
                                new Point(x2,y2),
                                new Point(x3,y3),
                                new Point(x4,y4),
                                angle
                        );*/

                        GlobalResources.getInstance().getDevices().getMap().put(device, new Position(x,y,rotation, z));
                    }
                }
                long timeDif = LatencyTest.getLatency();
                //Log.i("Byte",""+b);
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
            id.close();
            Log.i("Connection", "[ OK ] InputStream Closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
