package com.EE5.communications;

import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.EE5.R;
import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.server.BluetoothServer;
import com.EE5.server.Server;
import com.EE5.server.ServerPassThrough;
import com.EE5.server.TCPServer;
import com.EE5.server.data.Position;
import com.EE5.server.socketTask.SocketTaskType;
import com.EE5.util.GlobalResources;

import java.util.ArrayList;
import java.util.Map;

public class ServerActivity extends ActionBarActivity{
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
                /*if(msg.what==0){
                    ServerActivity.this.updateList();
                }*/
                if(msg.what==1){
                    ServerActivity.this.updatePositionsList();
                }
                super.handleMessage(msg);
            }
        };

        String type = sharedPref.getString("connection_type", "TCP");
        String connection_handling = sharedPref.getString("socketTaskType", "PRIMITIVE_DATA");
        SocketTaskType socketTaskType = SocketTaskType.valueOf(connection_handling); //Convert string to equivalent Enumeration value.

        if(type.equals("Bluetooth")){
            this.server = new BluetoothServer(socketTaskType, handler);
        }
        else {
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
        this.server.start();
        GlobalResources.getInstance().setServer(this.server);

        //Also store the values of this device on the server.
        ServerPassThrough passThrough = new ServerPassThrough();
        passThrough.startPolling();
    }

    private void updatePositionsList() {
        ListView lstServerConnections = (ListView) findViewById(R.id.lst_server_connections);
        ArrayAdapter adapter = (ArrayAdapter<String>) lstServerConnections.getAdapter();
        adapter.clear();
        /*for (Map.Entry<String, Position> entry : this.server.getConnectedDevices().getMap().entrySet())
        {
            String id = entry.getKey();
            Position pos = entry.getValue();
            adapter.insert(""+id+"\n("+pos.getX()+","+pos.getY()+","+pos.getRotation()+","+pos.getHeight()+")", 0);
        }*/
        for (Map.Entry<String, PatternCoordinator> entry : this.server.getConnectedDevices().getPatternMap().entrySet())
        {
            String id = entry.getKey();
            PatternCoordinator pc = entry.getValue();
            adapter.insert(""+id+"\n("+Math.round(pc.getNum1().x)+","+Math.round(pc.getNum1().y)+")  ("
                    +Math.round(pc.getNum2().x)+","+Math.round(pc.getNum2().y)+")  ("
                    +Math.round(pc.getNum3().x)+","+Math.round(pc.getNum3().y)+")  ("
                    +Math.round(pc.getNum4().x)+","+Math.round(pc.getNum4().y)+")"
                    , 0);
        }
        ((ArrayAdapter) lstServerConnections.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_server, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void updateList(){
        ListView lstServerConnections = (ListView) findViewById(R.id.lst_server_connections);
        ArrayAdapter adapter = (ArrayAdapter<String>) lstServerConnections.getAdapter();
        adapter.clear();
        for (Map.Entry<String, Position> entry : this.server.getConnectedDevices().getMap().entrySet())
        {
            String id = entry.getKey();
            adapter.insert(""+id, 0);
        }
        ((ArrayAdapter) lstServerConnections.getAdapter()).notifyDataSetChanged();
    }
}
