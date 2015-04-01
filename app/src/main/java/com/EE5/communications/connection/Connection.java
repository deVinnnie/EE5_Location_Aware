package com.EE5.communications.connection;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.EE5.client.AbstractClientOutputThread;
import com.EE5.client.Client;
import com.EE5.image_manipulation.ImageManipulationsActivity;
import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.server.data.Device;
import com.EE5.server.data.Position;
import com.EE5.server.socketTask.SocketTaskType;
import com.EE5.util.ConnectionException;

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

    private Position currentPosition = new Position(0,10,3.14,5);

    //Runs without a timer by reposting this handler at the end of the runnable
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable(){
        @Override
        public void run() {
            try {
                PatternCoordinator pc = ImageManipulationsActivity.patternCoordinator;
                currentPosition = new Position(pc.getNum2().x, pc.getNum2().y, currentPosition.getRotation(), currentPosition.getHeight());

                Device device = Client.currentDevice;
                device.setPosition(currentPosition);
                ((AbstractClientOutputThread) client.getClientOutputThread()).setDevice(device);

                /*TextView txtIPAddress = (TextView) findViewById(R.id.txtPosition);
                txtIPAddress.setText("Position: (" + currentPosition.getX() + "," + currentPosition.getY() + ")");*/
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

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    //</editor-fold>

    protected void waitForConnection(Client client, Object mutex) throws ConnectionException {
        client.execute();

        Log.i("Connection", "[ Blocked ] Waiting for connection...");
        synchronized (mutex) {
            //Avoid sending packets while the connection is still being established.
            try {
                mutex.wait(); //Notified when client.start() has ended.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.i("Connection", "[ OK ] Continued.");
        if(!client.isConnected()){
            throw new ConnectionException("Connection failed.");
        }

        this.setClient(client);
    }
}
