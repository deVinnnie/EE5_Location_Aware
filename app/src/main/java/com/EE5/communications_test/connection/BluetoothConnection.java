package com.EE5.communications_test.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.EE5.client.Client;
import com.EE5.client.bluetooth_client.BluetoothClient;
import com.EE5.server.socketTask.SocketTaskType;

import java.util.Set;

public class BluetoothConnection implements Connection{
    private ArrayAdapter<String> historyAdapter;
    private SocketTaskType socketTaskType;

    public BluetoothConnection(ArrayAdapter<String> historyAdapter, SocketTaskType socketTaskType){
        this.historyAdapter = historyAdapter;
        this.socketTaskType = socketTaskType;
    }

    @Override
    public Client connect() {
        //Setup Client Connection
        final Object mutex = new Object();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set bondendDevices = mBluetoothAdapter.getBondedDevices();
        Object[] deviceArray = bondendDevices.toArray();
        BluetoothDevice mmDevice = (BluetoothDevice) deviceArray[0];

        Log.i("Devices",mmDevice.getAddress());

        Client client = new BluetoothClient(mutex, historyAdapter, socketTaskType,  mmDevice);
        client.execute();

        Log.i("Connection","[ Blocked ] Waiting for connection...");
        synchronized (mutex) {
            //Avoid sending packets while the connection is still being established.
            try {
                mutex.wait(); //Notified when client.start() has ended.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.i("Connection","[ OK ] Continued.");
        return client;
    }
}
