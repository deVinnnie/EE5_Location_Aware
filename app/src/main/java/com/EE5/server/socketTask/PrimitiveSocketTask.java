package com.EE5.server.socketTask;

import android.util.Log;

import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.server.Server;
import com.EE5.server.data.Position;

import org.opencv.core.Point;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Each SocketTask handles a connection between the server and one client.
 * Multiple SocketTasks may be running to allow multiple connections.
 * The server must make sure each SocketTask is started on its own thread.
 *
 * PrimitiveSocketTask uses the DataInputStream and DataOutputStream classes.
 * It chunks each part of the data and sends it as a primitive data type.
 */
public class PrimitiveSocketTask extends SocketTask {

    public PrimitiveSocketTask(Socket socket, Server server) throws IOException {
        super(socket, server);
    }

    public PrimitiveSocketTask(InputStream in, OutputStream out, Server server){
        super(in, out, server);
    }

    @Override
    public void run(){
        try{
            //Note: Input stream and output stream should be created in 'opposite' order for client and server.
            //ObjectInputStream or ObjectOutputStream will hang/block until the connection is established.
            //The order of initialisation is therefore important:
            //
            //Server:
            //  create input stream
            //  create output stream
            //
            //Client:
            //  create output stream
            //  create input stream.
            //
            // See also: http://stackoverflow.com/questions/9148945/program-hangs-on-objectinputstream-readobject-method
            //ObjectInputStream oInputStream = new ObjectInputStream(socket.getInputStream()); //ObjectInputStream => Deserialises objects
            //ObjectOutputStream oOutputStream = new ObjectOutputStream(this.getOutputStream()); //Serializes objects.
            InputStream i = this.getInputStream();
            OutputStream o = this.getOutputStream();

            DataInputStream id = new DataInputStream(i);
            DataOutputStream od = new DataOutputStream(o);

            String uuid = id.readUTF();
            Log.i("UUID",uuid);
            this.getServer().getDevices().getMap().put(uuid,new Position(0,0,0,0));

            //Keep reading from socket.
            while(true){
                /*double posX = id.readDouble();
                double posY = id.readDouble();
                double rotation = id.readDouble();*/
                double x1 = id.readDouble();
                double y1 = id.readDouble();

                double x2 = id.readDouble();
                double y2 = id.readDouble();

                double x3 = id.readDouble();
                double y3 = id.readDouble();

                double x4 = id.readDouble();
                double y4 = id.readDouble();

                double angle = id.readDouble();

                PatternCoordinator pattern = new PatternCoordinator(
                        new Point(x1,y1),
                        new Point(x2,y2),
                        new Point(x3,y3),
                        new Point(x4,y4),
                        angle
                );

                //Log.i("Position", "" + "(" + posX + "," + posY  +"," + rotation + ")");

                //Position position = new Position(posX, posY, rotation,0);
                this.getServer().getDevices().getPatternMap().put(uuid,pattern);
                //Iterate over all devices and send the stored position over the current connection.
                for (Map.Entry<String, PatternCoordinator> entry : this.getServer().getDevices().getPatternMap().entrySet())
                {
                    //Send position when the device is not the originating device.
                   /* if(!entry.getKey().equals(uuid)) {
                        Log.i("ID", entry.getKey());
                        od.writeUTF(entry.getKey());
                        Position pos = entry.getValue();

                        od.writeDouble(pos.getX());
                        od.writeDouble(pos.getY());
                        od.writeDouble(pos.getRotation());
                    }*/
                    if(!entry.getKey().equals(uuid)) {
                        Log.i("ID", entry.getKey());
                        od.writeUTF(entry.getKey());
                        PatternCoordinator pc = entry.getValue();

                        od.writeDouble(pc.getNum1().x);
                        od.writeDouble(pc.getNum1().y);

                        od.writeDouble(pc.getNum2().x);
                        od.writeDouble(pc.getNum2().y);

                        od.writeDouble(pc.getNum3().x);
                        od.writeDouble(pc.getNum3().y);

                        od.writeDouble(pc.getNum4().x);
                        od.writeDouble(pc.getNum4().y);

                        od.writeDouble(pc.getAngle());
                    }
                }

                this.getServer().alertify(1,null, 1);

                //Send "000" to signal end of transmission.
                od.writeUTF("000");
                od.flush();
            }

            //oOutputStream.close();
            //oInputStream.close();
            //Log.i("Server", "[ OK ] OutputStream and InputStream closed.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
