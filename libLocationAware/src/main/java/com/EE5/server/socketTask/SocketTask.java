package com.EE5.server.socketTask;

import com.EE5.server.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class SocketTask implements Runnable{
    private InputStream inputStream;
    private OutputStream outputStream;
    private Server server;

    public SocketTask(){}

    public SocketTask(Socket socket, Server server) throws IOException {
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.server = server;
    }

    public SocketTask(InputStream in, OutputStream out, Server server){
        this.inputStream = in;
        this.outputStream = out;
        this.server = server;
    }

    public Server getServer(){
        return this.server;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
