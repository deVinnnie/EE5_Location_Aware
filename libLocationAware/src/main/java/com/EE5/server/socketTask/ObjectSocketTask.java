package com.EE5.server.socketTask;

import android.util.Log;

import com.EE5.server.Server;
import com.EE5.server.data.Device;
import com.EE5.server.data.DeviceList;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Each SocketTask handles a connection between the server and one client.
 * Multiple SocketTasks may be running to allow multiple connections.
 * The server must make sure each SocketTask is started on its own thread.
 *
 * ObjectSocketTask uses object serialisation and utilizes the ObjectInputStream and ObjectOutputStream classses.
 */
public class ObjectSocketTask extends SocketTask{
    private Device device;

    public ObjectSocketTask(Socket socket, Server server) throws IOException {
        super(socket, server);
    }

    public ObjectSocketTask(InputStream in, OutputStream out, Server server){
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
            ObjectInputStream oInputStream = new ObjectInputStream(this.getInputStream()); //ObjectInputStream => Deserialises objects
            ObjectOutputStream oOutputStream = new ObjectOutputStream(this.getOutputStream()); //Serializes objects.

            Log.i("Server", "[ OK ] ObjectStreams Created.");

            DeviceList devices = this.getServer().getDevices();
            GlobalResources.getInstance().setDevices(devices);

            //Keep reading from socket.
            while(true){
                Device device = (Device) oInputStream.readObject(); //Will block until it has received a new object.
                if(device == null){
                    devices.removeDevice(this.device);
                    //this.getServer().alertify(0,null, 1);
                    //Log.d("Server", "Removing Device");
                    break;
                }
                this.device = device;
                boolean present = devices.contains(device);
                devices.addDevice(device);

                this.getServer().alertify(1,null, 1);

                if(!present) {
                   // this.getServer().alertify(0,null, 1);
                }

                //System.out.print("Position: (" + device.getPosition().getX() + "," + device.getPosition().getY() + ")\n");
                // "\r" Is the carriage return, it places the cursor back at the beginning of the line.
                // At the next print, the previously present text is overwritten.

                //Send something back to client. */
                oOutputStream.reset();
                oOutputStream.writeObject(this.getServer().getDevices());
                oOutputStream.flush();
            }

            oOutputStream.close();
            oInputStream.close();
            Log.i("Server", "[ OK ] OutputStream and InputStream closed.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
