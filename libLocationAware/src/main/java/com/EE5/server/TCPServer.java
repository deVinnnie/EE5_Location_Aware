package com.EE5.server;

import android.os.Handler;
import android.util.Log;

import com.EE5.server.socketTask.SocketTaskFactory;
import com.EE5.server.socketTask.SocketTaskType;
import com.EE5.util.MyDate;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Server{
    private ServerSocket serverSocket;

    public TCPServer(SocketTaskType socketTaskType){
        super(socketTaskType);
    }

    public TCPServer(int SERVER_PORT, SocketTaskType socketTaskType, Handler handler) throws IOException {
        super(socketTaskType,handler);
        serverSocket = new ServerSocket(SERVER_PORT);
    }

    /**
     * Listen for connections and dispatch on a new SocketTask thread.
     */
    @Override
    public void run() {
        try{
            while(this.isStarted()) {
                //Keep accepting new connections and put them on a new thread. 
                Socket socket = serverSocket.accept();

                String IPAdress = socket.getInetAddress().toString();

                System.out.println("["+ MyDate.getDate()+ "]" + "Incoming connection from " + IPAdress);
                Log.i("Server","["+ MyDate.getDate()+ "]" + "Incoming connection from " + IPAdress);
                if (socket.isConnected()){
                    this.getExecutorService().execute(SocketTaskFactory.produce(this.getSocketTaskType(), socket.getInputStream(), socket.getOutputStream(), this)); //Execute on a new Thread.
                }
            }
            if (serverSocket != null){
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Safely shut down server.
     */
    @Override
    public void quit() throws IOException{
        super.quit();
        if(serverSocket != null) {
            serverSocket.close();
        }
    }
}