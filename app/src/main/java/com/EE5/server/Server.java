package com.EE5.server;

import android.os.Handler;
import android.os.Message;

import com.EE5.server.data.DeviceList;
import com.EE5.server.socketTask.SocketTaskType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Listens and handles incoming connections.
 * In Android all networking must be done on a separate thread.
 */
public class Server extends Thread{
    private ExecutorService executorService;
    private boolean isStarted = true;
    private DeviceList devices = new DeviceList();
    private SocketTaskType socketTaskType;

    /**
     * Handler for access to UI Thread.
     * The User Interface may only be updated from within the UI thread.
     * This handler allows the Server thread to send a message to the UI thread,
     * which in turn can update the User Interface.
     */
    private Handler handler;

    public Server(SocketTaskType socketTaskType){
        this.socketTaskType = socketTaskType;
        this.executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 50
        );
    }

    public Server(SocketTaskType socketTaskType, Handler handler){
        this(socketTaskType);
        this.handler = handler;
    }

    /**
     * Send message to the UI thread.
     * This method gets and sends a message with the input parameters of the method.
     *
     * @param what
     * @param obj
     * @param arg1
     */
    public void alertify(int what, Object obj, int arg1){
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        msg.arg1 = arg1;
        handler.sendMessage(msg);
    }

    //<editor-fold desc="Getters & Setters">
    public ExecutorService getExecutorService() {
        return executorService;
    }

    public DeviceList getConnectedDevices(){
        return this.devices;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public DeviceList getDevices() {
        return devices;
    }

    public void setDevices(DeviceList devices) {
        this.devices = devices;
    }

    public SocketTaskType getSocketTaskType() {
        return socketTaskType;
    }
    //</editor-fold>
}
