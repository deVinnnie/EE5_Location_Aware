package com.EE5.server;

import android.os.Handler;

import com.EE5.image_manipulation.ImageManipulationsActivity;
import com.EE5.image_manipulation.PatternCoordinator;
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
                PatternCoordinator pc = ImageManipulationsActivity.patternCoordinator;
                //currentPosition = new Position(pc.getNum1().x, pc.getNum1().y, currentPosition.getRotation(), currentPosition.getHeight());

                Server server = GlobalResources.getInstance().getServer();
                server.getDevices().getPatternMap().put("0001", pc);
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
