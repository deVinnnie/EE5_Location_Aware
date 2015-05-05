package com.EE5.client;

import com.EE5.server.data.Device;

import java.io.OutputStream;

public abstract class AbstractClientOutputThread extends AbstractClientThread {
    private OutputStream outputStream;
    private Device device;

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public AbstractClientOutputThread(OutputStream out, Client client){
        super("Output_Thread", client);
        this.outputStream = out;
    }

    public void setDevice(Device device) {
        this.device = device;
        synchronized (this) {
            //Notify that new message has arrived.
            notify();
        }
    }

    public Device getDevice(){
        return this.device;
    }
}
