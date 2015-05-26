package com.EE5.communications.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.EE5.client.Client;
import com.EE5.client.bluetooth_client.BluetoothClient;
import com.EE5.server.socketTask.SocketTaskType;
import com.EE5.util.ConnectionException;

import java.util.Set;

public class BluetoothConnection extends Connection{
    /**
     * Index of the target device in the mBluetoothAdapter.getBondedDevices().
     */
    private int device;

    public BluetoothConnection(int sampleRate, ArrayAdapter<String> historyAdapter, SocketTaskType socketTaskType, Context context){
        super(sampleRate, historyAdapter, socketTaskType, context);
    }

    @Override
    public Client connect() throws ConnectionException{
        try {
            //Setup Client Connection
            final Object mutex = new Object();
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set bondedDevices = mBluetoothAdapter.getBondedDevices();
            Object[] deviceArray = bondedDevices.toArray();
            BluetoothDevice mmDevice = (BluetoothDevice) deviceArray[this.device];

            Log.i("Devices", mmDevice.getAddress());

            Client client = new BluetoothClient(mutex, this.getHistoryAdapter(), this.getSocketTaskType(), this.getContext(), mmDevice);
            this.waitForConnection(client, mutex);
            return client;
        }
        catch(ArrayIndexOutOfBoundsException e){
            throw new ConnectionException("No Bluetooth devices connected", e);
        }
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }
}
