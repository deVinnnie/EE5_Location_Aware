package com.EE5.communications_test;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.EE5.R;
import com.EE5.client.Client;
import com.EE5.client.IDGenerator;
import com.EE5.client.Transmitter;
import com.EE5.communications_test.connection.ConnectionFactory;
import com.EE5.image_manipulation.ImageManipulationsActivity;
import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.server.data.Device;
import com.EE5.server.data.Position;

import java.util.ArrayList;

public class PositionActivity extends ActionBarActivity {
    private String ipAddress = "192.168.42.18";

    /**
     * Server port for a TCP connection. (Usually 8080)
     */
    private int port = 8081;

    /**
     * Rate at which the position is sent to the server.
     * Specified in milliseconds.
     */
    private long sampleRate = 500;

    private Device device;
    private Client client;
    private ArrayList<String> history = new ArrayList<String>();
    private Position currentPosition = new Position(0,10,3.14,5);
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    private Button btnConnect;
    private Button btnDisconnect;

    //Runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                PatternCoordinator pc = ImageManipulationsActivity.patternCoordinator;
                currentPosition = new Position(pc.getNum2().x, pc.getNum2().y, currentPosition.getRotation(), currentPosition.getHeight());
                //currentPosition = new Position(currentPosition.getX()+1, currentPosition.getY(), currentPosition.getRotation(), currentPosition.getHeight());
                device.setPosition(currentPosition);
                ((Transmitter) client.getClientOutputThread()).setDevice(device);

                TextView txtIPAddress = (TextView) findViewById(R.id.txtPosition);
                txtIPAddress.setText("Position: (" + currentPosition.getX() + "," + currentPosition.getY() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Execute this code again after <this.sampleRate> miliseconds.
            timerHandler.postDelayed(timerRunnable, PositionActivity.this.sampleRate);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String deviceId = IDGenerator.generate(sharedPref);
        this.device = new Device(deviceId);
        Client.currentDevice = device;

        //Load Preferences
        this.loadPreferences();

        //This ensures that the preferences are reloaded whenever a change in made to them.
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                loadPreferences();
            }
        };
        sharedPref.registerOnSharedPreferenceChangeListener(prefListener);

        //Hook up event listeners.
        //This connects the click on a button to the right method for execution.
        this.btnConnect = (Button) findViewById(R.id.buttonConnect);
        this.btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start timer.
                client = connect();
            }
        });

        this.btnDisconnect = (Button) findViewById(R.id.buttonDisconnect);
        this.btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnect();

            }
        });
        this.btnDisconnect.setEnabled(false);

        ListView lstConnectionHistory = (ListView) findViewById(R.id.lst_connectionHistory);
        TextView txt = new TextView(this.getApplicationContext());
        txt.setTextColor(0);
        lstConnectionHistory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, history) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.BLACK);
                return textView;
            }
        });
    }

    private void loadPreferences(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        this.ipAddress = sharedPref.getString("server_ip", "127.0.0.1"); //Second param is default value.
        this.port = Integer.parseInt(sharedPref.getString("server_port", "8080"));

        TextView txtIPAddress = (TextView) findViewById(R.id.txtIPAddress);
        txtIPAddress.setText("Server IP = " + this.ipAddress);

        this.sampleRate = Integer.parseInt(sharedPref.getString("sample_rate", "500"));
    }

    //<editor-fold desc="Connection">
    public Client connect(){
        Log.i("PositionActivity", "[ BUSY ] Connecting...");
        Log.i("PositionActivity", "[ BUSY ] Creating Client...");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>)((ListView) findViewById(R.id.lst_connectionHistory)).getAdapter();
        Client client = ConnectionFactory.produce(arrayAdapter, sharedPref);

        Log.i("PositionActivity", "[ OK ] Connection Setup.");
        timerHandler.postDelayed(timerRunnable, PositionActivity.this.sampleRate);
        Log.i("PositionActivity", "[ OK ] Started Polling.");

        this.btnConnect.setEnabled(false);
        this.btnDisconnect.setEnabled(true);
        return client;
    }

    public void disconnect(){
        this.client.quit();
        ((Transmitter) client.getClientOutputThread()).setDevice(null);
        timerHandler.removeCallbacks(timerRunnable);
        this.btnConnect.setEnabled(true);
        this.btnDisconnect.setEnabled(false);
    }
    //</editor-fold>
    //<editor-fold desc="Other">
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.menu_position, menu);

        //Fill the option menu with Menu Items to other activities.
        //Each Item is defined with with an intent specifying the class name of the corresponding activity.
        //Clicks on a menu item are handled in the onOptionsItemsSelected method.

        //Links to the Preferences screen.
        Intent prefsIntent = new Intent(this, SettingsActivity.class);
        MenuItem preferences = menu.findItem(R.id.action_settings);
        preferences.setIntent(prefsIntent);

        //Option to start as server.
        Intent serverIntent = new Intent(this, ServerActivity.class);
        MenuItem useAsServer = menu.findItem(R.id.action_use_as_server);
        useAsServer.setIntent(serverIntent);
*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       /* int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity (item.getIntent());
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
    //</editor-fold>
}