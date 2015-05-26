package com.EE5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.EE5.communications.ConnectionActivity;
import com.EE5.communications.ServerActivity;
import com.EE5.image_manipulation.ImageManipulationsActivity;
import com.EE5.image_manipulation.PatternDetector;
import com.EE5.preferences.SettingsActivity;
import com.EE5.server.data.Position;
import com.EE5.util.Axis;
import com.EE5.util.GlobalResources;
import com.EE5.util.Tuple;

import java.util.HashMap;
import java.util.Map;


public class SetupActivity extends Activity {

    private HashMap<String, Axis> otherDevices = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global, menu);

        //Link to the Preferences screen.
        Intent prefsIntent = new Intent(this, SettingsActivity.class);
        MenuItem preferences = menu.findItem(R.id.action_settings);
        preferences.setIntent(prefsIntent);

        //Option to start as server.
        Intent serverIntent = new Intent(this, ServerActivity.class);
        MenuItem useAsServer = menu.findItem(R.id.action_use_as_server);
        useAsServer.setIntent(serverIntent);

        //Option to start as client.
        Intent clientIntent = new Intent(this, ConnectionActivity.class);
        MenuItem useAsClient = menu.findItem(R.id.action_use_as_client);
        useAsClient.setIntent(clientIntent);

        //Option to start as client.
        Intent calibrateIntent = new Intent(this, ImageManipulationsActivity.class);
        MenuItem calibrate = menu.findItem(R.id.action_calibrate);
        calibrate.setIntent(calibrateIntent);

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        PatternDetector patternDetector = GlobalResources.getInstance().getPatternDetector();
        if(patternDetector != null) {
            patternDetector.destroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PatternDetector patternDetector = GlobalResources.getInstance().getPatternDetector();
        if(patternDetector != null) {
            patternDetector.setup();
        }
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable(){
        @Override
        public void run() {
            try {
                RelativeLayout r = (RelativeLayout) findViewById(R.id.test_canvas);
                Position devicePosition = GlobalResources.getInstance().getDevice().getPosition();
                int size = 100;
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(size,size);
                Axis iv = (Axis) findViewById(R.id.pushpin_device);
                rp.setMargins((int) (devicePosition.getX() + (r.getWidth() / 2.0) - (size / 2.0)),
                        (int) ((r.getHeight() / 2.0) - devicePosition.getY() - (size / 2.0))
                        , 0, 0);

                iv.setLayoutParams(rp);
                iv.setRotation((float) devicePosition.getRotation());

                for (Map.Entry<String, Tuple<Position,String>> entry : GlobalResources.getInstance().getDevices().getAll()) {
                    Position otherPosition = entry.getValue().element1;
                    String id = entry.getKey();
                    Axis axis;
                    if(otherDevices.containsKey(id)){
                        axis = otherDevices.get(id);
                    }
                    else {
                        LayoutInflater inflater = LayoutInflater.from(SetupActivity.this);
                        axis = (Axis) inflater.inflate(R.layout.axis, null, false);
                        otherDevices.put(id, axis);
                        r.addView(axis);
                    }

                    RelativeLayout.LayoutParams rp2 = new RelativeLayout.LayoutParams(size,size);
                    rp2.setMargins((int) (otherPosition.getX() + (r.getWidth() / 2.0) - (size / 2.0)),
                            (int) ((r.getHeight() / 2.0) - otherPosition.getY() - (size / 2.0))
                            , 0, 0);

                    axis.setLayoutParams(rp2);
                    axis.setRotation((float)otherPosition.getRotation());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Execute this code again after <this.sampleRate> milliseconds.
            timerHandler.postDelayed(timerRunnable, 100);
        }
    };
}
