package com.EE5.client;

import java.io.OutputStream;

public abstract class AbstractClientOutputThread extends AbstractClientThread {
    private OutputStream outputStream;

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public AbstractClientOutputThread(OutputStream out, Client client){
        super("Output_Thread", client);
        this.outputStream = out;
    }
}
