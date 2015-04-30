package com.EE5.client;

/**
 * Handles the incoming or outgoing stream of the connection.
 * Each must run on a separate thread so as to not conflict with each other.
 * Subclasses must override the {@link #run() run} method with their own behaviour.
 *
 */
public abstract class AbstractClientThread extends Thread{
    private boolean keepRunning = true;
    private Client client;

    public AbstractClientThread(){}

    public AbstractClientThread(String threadName){
        super(threadName);
    }

    public AbstractClientThread(String threadName, Client client){
        this(threadName);
        this.client = client;
    }

    public void setKeepRunning(boolean isStart) {
        this.keepRunning = isStart;
    }

    public boolean keepRunning(){
        return this.keepRunning;
    }

    public void stopRunning(){
        this.keepRunning = false;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public abstract void run();
}
