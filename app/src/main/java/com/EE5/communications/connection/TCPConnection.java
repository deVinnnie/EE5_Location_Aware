package com.EE5.communications.connection;

import android.widget.ArrayAdapter;

import com.EE5.client.Client;
import com.EE5.client.tcp_client.TCPClient;
import com.EE5.server.socketTask.SocketTaskType;

public class TCPConnection extends Connection{
    private String ipAddress;
    private int port;

    public TCPConnection(int sampleRate, ArrayAdapter<String> historyAdapter, SocketTaskType socketTaskType, String ipAddress, int port){
        super(sampleRate, historyAdapter, socketTaskType);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public Client connect() {
        //Setup Client Connection
        final Object mutex = new Object();
        Client client = new TCPClient(this.ipAddress,this.port, mutex, this.getHistoryAdapter(), this.getSocketTaskType());
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
