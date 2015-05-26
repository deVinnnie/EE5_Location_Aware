package com.EE5.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.EE5.server.socketTask.SocketTaskFactory;
import com.EE5.server.socketTask.SocketTaskType;

import java.io.IOException;
import java.util.UUID;

public class BluetoothServer extends Server {
    private BluetoothServerSocket mmServerSocket;
    private BluetoothAdapter mBluetoothAdapter;
    private static final String NAME = "BluetoothApp";

    public BluetoothServer(SocketTaskType socketTaskType, Handler handler) {
        super(socketTaskType, handler);
        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            // MY_UUID is the app's UUID string, also used by the client code
            mmServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("BS", "End of Server Constructor");
    }

    @Override
    public void run() {
        try {
            // Keep listening until exception occurs or a socket is returned
            Log.i("Bluetooth", "[ OK ] Listening for incoming connections.");
            while (true) {
                BluetoothSocket socket = mmServerSocket.accept();
                Log.i("BS", "New Connection");
                // If a connection was accepted

                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    this.getExecutorService().execute(SocketTaskFactory.produce(this.getSocketTaskType(), socket.getInputStream(), socket.getOutputStream(), this));
                    //mmServerSocket.close(); We should keep listening to incoming connections
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Will cancel the listening socket, and cause the thread to finish */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) { }
    }

    @Override
    public void quit() throws IOException{
        super.quit();
        mmServerSocket.close();
    }
}