package com.EE5.communications;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.EE5.R;
import com.EE5.client.Client;
import com.EE5.client.IDGenerator;
import com.EE5.communications.connection.Connection;
import com.EE5.communications.connection.ConnectionFactory;
import com.EE5.server.data.Device;
import com.EE5.util.ConnectionException;
import com.EE5.util.GlobalResources;

import java.util.ArrayList;

public class ConnectionActivity extends ActionBarActivity {
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
        ArrayAdapter<String> arrayAdapter;

        //Hook up event listeners.
        //This connects the click on a button to the right method for execution.
        this.btnConnect = (Button) findViewById(R.id.buttonConnect);
        this.btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        GlobalResources global = GlobalResources.getInstance();
        //If a connection is not yet made it will be null.
        if(global.getConnection() != null){
            //Retrieve the arrayAdapter used previously and later pass it through to the ListView.
            //The ListView will then display the (old) data present in the ArrayAdapter.
            arrayAdapter = global.getConnection().getHistoryAdapter();
            this.btnConnect.setEnabled(false);
            this.btnDisconnect.setEnabled(true);
        }
        else {
            //Generate a new ArrayAdapter when the connection is yet to be made.
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, new ArrayList<String>());
        }

        ListView lstConnectionHistory = (ListView) findViewById(R.id.lst_connectionHistory);
        lstConnectionHistory.setAdapter(arrayAdapter);

        //Load Preferences
        this.loadPreferences();

        //This ensures that the preferences are reloaded whenever a change in made to them.
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                loadPreferences();
            }
        };
        sharedPref.registerOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        //outState.putSerializable("someExpensiveObject", 1);
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
        try {
            Log.i("PositionActivity", "[ BUSY ] Connecting...");
            Log.i("PositionActivity", "[ BUSY ] Creating Client...");

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            ArrayAdapter<String> arrayAdapter = (ArrayAdapter<String>)((ListView) findViewById(R.id.lst_connectionHistory)).getAdapter();
            Connection connection = ConnectionFactory.produce(arrayAdapter, sharedPref, this.getApplicationContext());

            connection.connect();

            GlobalResources.getInstance().setConnection(connection);

            Log.i("PositionActivity", "[ OK ] Connection Setup.");
            connection.startPolling();
            Log.i("PositionActivity", "[ OK ] Started Polling.");

            this.btnConnect.setEnabled(false);
            this.btnDisconnect.setEnabled(true);

            finish();
        } catch (ConnectionException e) {
            CharSequence text = "Connection failed\n"+e.getMessage();
            Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    public void disconnect(){
        Connection connection = GlobalResources.getInstance().getConnection();
        connection.disconnect();
        this.btnConnect.setEnabled(true);
        this.btnDisconnect.setEnabled(false);
    }
    //</editor-fold>
}