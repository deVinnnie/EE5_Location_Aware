package com.EE5.server;

import android.os.Handler;

import com.EE5.util.GlobalResources;

/**
 * Passes the position of the server to the server.
 * This provides the same functionality as the client code, but without sending the data.
 * Data is injected directly using the static reference to the server instance.
 */
public class ServerPassThrough {
    private long sampleRate = 500;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable(){
        @Override
        public void run() {
            try {
                Server server = GlobalResources.getInstance().getServer();
                //server.getDevices().getPatternMap().put("0001", GlobalResources.getInstance().getDevice().getPattern());
                server.getDevices().getMap().put("0001", GlobalResources.getInstance().getDevice().getPosition());
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Execute this code again after <this.sampleRate> milliseconds.
            timerHandler.postDelayed(timerRunnable, sampleRate);
        }
    };

    public void startPolling(){
        timerHandler.postDelayed(timerRunnable, this.sampleRate);
    }

    public void stopPolling(){
        timerHandler.removeCallbacks(timerRunnable);
    }
}
