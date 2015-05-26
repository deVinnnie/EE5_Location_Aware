package com.EE5.communications;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.EE5.R;
import com.EE5.server.BluetoothServer;
import com.EE5.server.Server;
import com.EE5.server.ServerPassThrough;
import com.EE5.server.TCPServer;
import com.EE5.server.data.Position;
import com.EE5.server.socketTask.SocketTaskType;
import com.EE5.util.GlobalResources;
import com.EE5.util.Tuple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ServerActivity extends Activity {
    private Server server;
    private ArrayList<String> connectedDevices = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        Log.i("Server", "Creating Server Activity");
        ListView lstServerConnections = (ListView) findViewById(R.id.lst_server_connections);
        lstServerConnections.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, connectedDevices));

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    ServerActivity.this.updatePositionsList();
                }
                super.handleMessage(msg);
            }
        };

        String type = sharedPref.getString("connection_type", "TCP");
        String connection_handling = sharedPref.getString("socketTaskType", "PRIMITIVE_DATA");
        SocketTaskType socketTaskType = SocketTaskType.valueOf(connection_handling); //Convert string to equivalent Enumeration value.

        this.server = GlobalResources.getInstance().getServer();
        if(this.server == null) {
            if (type.equals("Bluetooth")) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                    //Check if bluetooth is enabled before starting the server, this prevents an application crash.
                    Toast.makeText(this.getApplicationContext(), "[Server] Bluetooth not active", Toast.LENGTH_LONG).show();
                }
                else {
                    this.server = new BluetoothServer(socketTaskType, handler);
                }
            }
            else {
                try {
                    //TCP - default.
                    //The TCP connection needs some more information to setup.
                    int port = Integer.parseInt(sharedPref.getString("server_port", "8080"));
                    this.server = new TCPServer(port, socketTaskType, handler);

                    //Get the IP address of the server from the Android WifiManager.
                    WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                    int ipAddress = wm.getConnectionInfo().getIpAddress();
                    //Format the IP address into something that is readable for humans.
                    String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
                    //Display the IP address in a TextView in large friendly letters.
                    TextView txtIPAddress = (TextView) findViewById(R.id.txtServerIP);
                    txtIPAddress.setText("Server IP = " + ip);
                }
                catch(IOException e){
                    CharSequence text = "[Server] Starting Server Failed\n"+e.getMessage();
                    Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }
            }

            if(this.server != null) {
                this.server.start();
                GlobalResources.getInstance().setServer(this.server);

                //Also store the values of this device on the server.
                ServerPassThrough passThrough = new ServerPassThrough();
                passThrough.startPolling();
            }
        }
        else{
            //Connect to existing server instance.
            this.server.setHandler(handler);
        }
    }

    private void updatePositionsList() {
        ListView lstServerConnections = (ListView) findViewById(R.id.lst_server_connections);
        ArrayAdapter adapter = (ArrayAdapter<String>) lstServerConnections.getAdapter();
        adapter.clear();
        for (Map.Entry<String, Tuple<Position, String>> entry : this.server.getConnectedDevices().getAll())
        {
            String id = entry.getKey();
            Position pos = entry.getValue().element1;
            String line = String.format("%s (x: %.2f, y: %.2f, rot: %.2f, z: %.2f)", id,
                                pos.getX(), pos.getY(), pos.getRotation(), pos.getHeight());
            adapter.insert(line,0);
        }
        ((ArrayAdapter) lstServerConnections.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server, menu);
        menu.add("Stop");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().equals("Stop") && this.server != null){
            //Stop Server.
            //TODO: More robust checking.
            try {
                this.server.quit();
                Toast.makeText(this.getApplicationContext(), "[Server] Server Stopped", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                CharSequence text = "[Server] Quitting Server Failed\n"+e.getMessage();
                Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
}
