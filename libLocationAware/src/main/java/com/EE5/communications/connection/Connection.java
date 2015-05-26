package com.EE5.communications.connection;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.EE5.client.AbstractClientOutputThread;
import com.EE5.client.Client;
import com.EE5.server.data.Device;
import com.EE5.server.socketTask.SocketTaskType;
import com.EE5.util.ConnectionException;
import com.EE5.util.GlobalResources;

public abstract class Connection {
    private Client client;
    private Context context;

    /**
     * Rate at which the position is sent to the server.
     * Specified in milliseconds.
     */
    private long sampleRate = 500;

    private ArrayAdapter<String> historyAdapter;
    private SocketTaskType socketTaskType;

    /**
     * The Handler stuff allows you to run code at specified times or time intervals.
     * Use in combination with runnable.
     */
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable(){
        @Override
        public void run() {
            try {
                Device device = GlobalResources.getInstance().getDevice();
                ((AbstractClientOutputThread) client.getClientOutputThread()).setDevice(device);
                //setDevice() will automatically trigger a transmission.
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Execute this code again after <this.sampleRate> milliseconds.
            timerHandler.postDelayed(timerRunnable, sampleRate);
        }
    };

    public Connection(){}

    public Connection(int sampleRate, ArrayAdapter<String> historyAdapter, SocketTaskType socketTaskType, Context context){
        this.sampleRate = sampleRate;
        this.historyAdapter = historyAdapter;
        this.socketTaskType = socketTaskType;
        this.context = context;
    }

    public abstract Client connect() throws ConnectionException;

    public void startPolling(){
        timerHandler.postDelayed(timerRunnable, this.sampleRate);
    }

    public void stopPolling(){
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void disconnect(){
        this.getClient().quit();
        ((AbstractClientOutputThread) this.getClient().getClientOutputThread()).setDevice(null);
        this.stopPolling();
    }

    //<editor-fold desc="Getters/Setters">
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ArrayAdapter<String> getHistoryAdapter() {
        return historyAdapter;
    }

    public void setHistoryAdapter(ArrayAdapter<String> historyAdapter) {
        this.historyAdapter = historyAdapter;
    }

    public long getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(long sampleRate) {
        this.sampleRate = sampleRate;
    }

    public SocketTaskType getSocketTaskType() {
        return socketTaskType;
    }

    public void setSocketTaskType(SocketTaskType socketTaskType) {
        this.socketTaskType = socketTaskType;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    //</editor-fold>

    /**
     * Block code execution until the connection is up and running.
     *
     * @param client
     * @param mutex
     * @throws ConnectionException
     */
    protected void waitForConnection(Client client, Object mutex) throws ConnectionException {
        client.execute();

        Log.i("Connection", "[ Blocked ] Waiting for connection to be established.");
        synchronized (mutex) {
            //Avoid sending packets while the connection is still being established.
            try {
                mutex.wait(); //Notified when client.start() has ended.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(!client.isConnected()){
            throw new ConnectionException("Connection failed. Client is not connected.");
        }

        Log.i("Connection", "[ OK ]");

        this.setClient(client);
    }
}
