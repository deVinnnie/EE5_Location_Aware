package com.EE5.communications.connection;

import android.os.Handler;
import android.widget.ArrayAdapter;

import com.EE5.client.AbstractClientOutputThread;
import com.EE5.client.Client;
import com.EE5.image_manipulation.ImageManipulationsActivity;
import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.server.data.Device;
import com.EE5.server.data.Position;
import com.EE5.server.socketTask.SocketTaskType;

public abstract class Connection {
    private Client client;

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

    public Connection(int sampleRate, ArrayAdapter<String> historyAdapter, SocketTaskType socketTaskType){
        this.sampleRate = sampleRate;
        this.historyAdapter = historyAdapter;
        this.socketTaskType = socketTaskType;
    }

    public abstract Client connect();

    public void startPolling(){
        timerHandler.postDelayed(timerRunnable, this.sampleRate);
    }

    public void stopPolling(){
        timerHandler.removeCallbacks(timerRunnable);
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

    //</editor-fold>
}
