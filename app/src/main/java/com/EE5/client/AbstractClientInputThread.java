package com.EE5.client;

import java.io.InputStream;

public abstract class AbstractClientInputThread extends AbstractClientThread{
    private InputStream inputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public AbstractClientInputThread(InputStream in, Client client){
        super("Input_Thread", client);
        this.inputStream = in;
    }
}
