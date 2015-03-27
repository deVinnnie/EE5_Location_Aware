package com.EE5.server.socketTask;

import android.util.Log;

import com.EE5.server.Server;
import com.EE5.server.data.Position;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class ByteSocketTask extends SocketTask {

    public ByteSocketTask(Socket socket, Server server) throws IOException {
        super(socket, server);
    }

    public ByteSocketTask(InputStream in, OutputStream out, Server server){
        super(in, out, server);
    }

    @Override
    public void run(){
        try{
            //Note: Input stream and output stream should be created in 'opposite' order for client and server.
            //ObjectInputStream or ObjectOutputStream will hang/block untill the connection is established.
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
                double posX = id.readDouble();
                double posY = id.readDouble();
                double rotation = id.readDouble();

                Log.i("Position", "" + "(" + posX + "," + posY  +"," + rotation + ")");

                Position position = new Position(posX, posY, rotation,0);
                this.getServer().getDevices().getMap().put(uuid,position);
                //Iterate over all devices and send the stored position over the current connection.
                for (Map.Entry<String, Position> entry : this.getServer().getDevices().getMap().entrySet())
                {
                    //Send position when the device is not the originating device.
                    if(!entry.getKey().equals(uuid)) {
                        Log.i("ID", entry.getKey());
                        od.writeUTF(entry.getKey());
                        Position pos = entry.getValue();

                        od.writeDouble(pos.getX());
                        od.writeDouble(pos.getY());
                        od.writeDouble(pos.getRotation());
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
