package com.EE5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.EE5.communications.ConnectionActivity;
import com.EE5.communications.ServerActivity;
import com.EE5.image_manipulation.ImageManipulationsActivity;
import com.EE5.preferences.SettingsActivity;


public class SetupActivity extends Activity {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(item.getIntent());
            return true;
        }


        if (id == R.id.action_calibrate){
        }
        return super.onOptionsItemSelected(item);
    }
}
