package com.EE5.communications_test.connection;

import android.widget.ArrayAdapter;

import com.EE5.client.Client;
import com.EE5.client.tcp_client.TCPClient;
import com.EE5.server.socketTask.SocketTaskType;

public class TCPConnection implements Connection{
    private String ipAddress;
    private int port;
    private ArrayAdapter<String> historyAdapter;
    private SocketTaskType socketTaskType;

    public TCPConnection(String ipAddress, int port, ArrayAdapter<String> historyAdapter, SocketTaskType socketTaskType){
        this.ipAddress = ipAddress;
        this.port = port;
        this.historyAdapter = historyAdapter;
        this.socketTaskType = socketTaskType;
    }

    @Override
    public Client connect() {
        //Setup Client Connection
        final Object mutex = new Object();
        Client client = new TCPClient(this.ipAddress,this.port, mutex, this.historyAdapter, socketTaskType);
        client.execute();

        synchronized (mutex) {
            //Avoid sending packets while the connection is still being established.
            try {
                mutex.wait(); //Notified when client.start() has ended.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return client;
    }
}
