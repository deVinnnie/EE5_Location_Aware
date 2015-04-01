package com.EE5.communications.connection;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.EE5.client.Client;
import com.EE5.client.tcp_client.TCPClient;
import com.EE5.server.socketTask.SocketTaskType;
import com.EE5.util.ConnectionException;

public class TCPConnection extends Connection{
    private String ipAddress;
    private int port;

    public TCPConnection(int sampleRate, ArrayAdapter<String> historyAdapter, SocketTaskType socketTaskType, Context context, String ipAddress, int port){
        super(sampleRate, historyAdapter, socketTaskType, context);
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public Client connect() throws ConnectionException {
        //Setup Client Connection
        final Object mutex = new Object();
        Client client = new TCPClient(this.ipAddress,this.port, mutex, this.getHistoryAdapter(), this.getSocketTaskType(), this.getContext());
        this.waitForConnection(client, mutex);
        return client;
    }
}
