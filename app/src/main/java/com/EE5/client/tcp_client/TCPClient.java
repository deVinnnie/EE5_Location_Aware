package com.EE5.client.tcp_client;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.EE5.client.AbstractClientInputThread;
import com.EE5.client.AbstractClientOutputThread;
import com.EE5.client.Client;
import com.EE5.client.ClientThreadFactory;
import com.EE5.server.socketTask.SocketTaskType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * TCPClient is an implementation of {@link Client} for connections WIFI using the TCP protocol.
 * It relies on Java Sockets for the connection.
 */
public class TCPClient extends Client {
    private String ip;
    private int port;
    private Socket socket;

    /**
     *
     * @param ip IP Address of the server instance.
     * @param port Port number of the server instance.
     */
    public TCPClient(String ip, int port, Object mutex, ArrayAdapter<String> historyAdapter, SocketTaskType socketTaskType) {
        super(mutex, historyAdapter, socketTaskType);
        this.ip = ip;
        this.port = port;
    }

    /**
     *
     * @return True when connection successful, false when failed.
     * @throws java.io.IOException
     */
    @Override
    public boolean start() throws IOException{
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(ip, port), 3000);
        boolean result = this.socket.isConnected();

        if (result) {
            Log.i("Connection", "Connection OK.");
            //Order of initialisation is important!!!
            SocketTaskType socketTaskType = this.getSocketTaskType();
            AbstractClientOutputThread outputThread = ClientThreadFactory.produceOutputThread(socketTaskType,socket.getOutputStream(), this);
            AbstractClientInputThread inputThread = ClientThreadFactory.produceInputThread(socketTaskType,socket.getInputStream(), this);
            this.setClientOutputThread(outputThread);
            this.setClientInputThread(inputThread);
            this.setIsStart(true);
            this.getClientInputThread().start();
            this.getClientOutputThread().start();
        }
        else{
            Log.i("Connection", "Connection failed.");
        }
        synchronized (this.getMutex()) {
            this.getMutex().notify();
        }
        return result;
    }

    @Override
    public void quit(){
        super.quit();
        try {
            if(socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
