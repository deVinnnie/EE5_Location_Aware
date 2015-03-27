package com.EE5.communications;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.EE5.R;
import com.EE5.client.AbstractClientOutputThread;
import com.EE5.client.Client;
import com.EE5.client.IDGenerator;
import com.EE5.communications.connection.Connection;
import com.EE5.communications.connection.ConnectionFactory;
import com.EE5.server.data.Device;
import com.EE5.util.GlobalResources;

import java.util.ArrayList;

public class ConnectionActivity extends ActionBarActivity {
    private Connection connection;
    private ArrayList<String> history = new ArrayList<String>();
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    private Button btnConnect;
    private Button btnDisconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String deviceId = IDGenerator.generate(sharedPref);
        Client.currentDevice = new Device(deviceId);

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
                connect();
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
        String ipAddress = sharedPref.getString("server_ip", "127.0.0.1"); //Second param is default value.
        //this.port = Integer.parseInt(sharedPref.getString("server_port", "8080"));

        TextView txtIPAddress = (TextView) findViewById(R.id.txtIPAddress);
        txtIPAddress.setText("Server IP = " + ipAddress);
    }

    //<editor-fold desc="Connection">
    public void connect(){
        Log.i("PositionActivity", "[ BUSY ] Connecting...");
        Log.i("PositionActivity", "[ BUSY ] Creating Client...");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>)((ListView) findViewById(R.id.lst_connectionHistory)).getAdapter();
        this.connection = ConnectionFactory.produce(arrayAdapter, sharedPref);
        connection.connect();
        GlobalResources.getInstance().setConnection(connection);

        Log.i("PositionActivity", "[ OK ] Connection Setup.");
        connection.startPolling();
        Log.i("PositionActivity", "[ OK ] Started Polling.");

        this.btnConnect.setEnabled(false);
        this.btnDisconnect.setEnabled(true);
    }

    public void disconnect(){
        this.connection.getClient().quit();
        ((AbstractClientOutputThread) connection.getClient().getClientOutputThread()).setDevice(null);
        connection.stopPolling();
        this.btnConnect.setEnabled(true);
        this.btnDisconnect.setEnabled(false);
    }
    //</editor-fold>
}