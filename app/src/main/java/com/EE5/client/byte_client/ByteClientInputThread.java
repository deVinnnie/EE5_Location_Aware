package com.EE5.client.byte_client;

import android.util.Log;

import com.EE5.client.AbstractClientInputThread;
import com.EE5.client.Client;
import com.EE5.client.LatencyTest;
import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.util.GlobalResources;

import org.opencv.core.Point;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

//TODO: Rename class to something more suitable.
public class ByteClientInputThread extends AbstractClientInputThread {
    private DataInputStream id;

    public ByteClientInputThread(InputStream in, Client client) {
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
                        /*double x = id.readDouble();
                        double y = id.readDouble();
                        double rot = id.readDouble();*/

                        double x1 = id.readDouble();
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
                        );

                        GlobalResources.getInstance().getDevices().getPatternMap().put(device, pc);
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
