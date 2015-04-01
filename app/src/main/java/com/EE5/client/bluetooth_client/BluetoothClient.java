package com.EE5.client.bluetooth_client;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.EE5.client.AbstractClientInputThread;
import com.EE5.client.AbstractClientOutputThread;
import com.EE5.client.Client;
import com.EE5.client.ClientThreadFactory;
import com.EE5.server.socketTask.SocketTaskType;
import com.EE5.util.ConnectionException;

import java.io.IOException;
import java.util.UUID;

/**
 * BluetoothClient is an implementation of {@link Client} for connections using the Bluetooth connection.
 * Android Bluetooth-sockets are used, which are nearly identical in use to the Java Network Sockets.
 * The BluetoothSocket and Socket classes do not have a common super class. But they do use the same Input and Outputstreams.
 */
public class BluetoothClient extends Client {
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;

    public BluetoothClient(Object mutex, ArrayAdapter<String> historyAdapter, SocketTaskType socketTaskType, Context context, BluetoothDevice device) {
        super(mutex, historyAdapter, socketTaskType, context);
        this.mmDevice = device;
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

    /**
     * @return True when connection successful, false when failed.
     * @throws IOException
     */
    @Override
    public boolean start() throws ConnectionException {
        try {
            Log.i("Connection", "[ - ] Bluetooth connecting...");
            boolean result;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            // MY_UUID is the app's UUID string, also used by the server code
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66"));
            mmSocket.connect();

            if (mmSocket.isConnected()) {
                Log.i("Connection", "[ OK ] Connection Successful.");
                //Order of initialisation is important!!!

                Log.i("Connection", "Creating ClientOutputThread");
                SocketTaskType socketTaskType = this.getSocketTaskType();
                AbstractClientOutputThread outputThread = ClientThreadFactory.produceOutputThread(socketTaskType, mmSocket.getOutputStream(), this);
                AbstractClientInputThread inputThread = ClientThreadFactory.produceInputThread(socketTaskType,mmSocket.getInputStream(), this);
                this.setClientOutputThread(outputThread);

                Log.i("Connection", "Creating ClientInputThread");
                //this.inputThread = new BluetoothClientInputThread(mmSocket, this);
                this.setClientInputThread(inputThread);

                Log.i("Connection", "Setting Start.");
                this.setIsStart(true);
                Log.i("Connection", "Starting input and output threads.");
                this.getClientOutputThread().start();
                this.getClientInputThread().start();
                result = true;
            }
            else{
                Log.i("Connection", "Connection failed.");
                result = false;
            }
            Log.i("Connection", "[ BUSY ] Preparing to release lock.");
            synchronized (this.getMutex()) {
                this.getMutex().notify();
                Log.i("Connection","[ OK ] Released lock");
            }
            return result;
        }
        catch (Exception e) {
            throw new ConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public void quit(){
        super.quit();
        try {
            if(mmSocket !=null) {
                this.mmSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
