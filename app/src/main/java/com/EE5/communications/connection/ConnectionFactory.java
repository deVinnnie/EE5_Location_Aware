package com.EE5.communications.connection;

import android.content.SharedPreferences;
import android.widget.ArrayAdapter;

import com.EE5.server.socketTask.SocketTaskType;

public class ConnectionFactory {
    public static Connection produce(ArrayAdapter arrayAdapter, SharedPreferences preferences) {
        Connection connection;
        String connection_type = preferences.getString("connection_type", "TCP");
        String connection_handling = preferences.getString("socketTaskType", "PRIMITIVE_DATA");
        SocketTaskType socketTaskType = SocketTaskType.valueOf(connection_handling); //Convert string to equivalent Enumeration value.
        int sampleRate = Integer.parseInt(preferences.getString("sample_rate", "500"));

        if (connection_type.equals("Bluetooth")) {
            connection = new BluetoothConnection(sampleRate, arrayAdapter, socketTaskType);
        }
        else {
            String ipAddress = preferences.getString("server_ip", "127.0.0.1"); //Second param is default value.
            int port = Integer.parseInt(preferences.getString("server_port", "8080"));
            connection = new TCPConnection(sampleRate, arrayAdapter, socketTaskType, ipAddress, port);
        }
        return connection;
    }
}
