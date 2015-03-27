package com.EE5.communications_test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.EE5.R;
import com.EE5.client.Client;
import com.EE5.server.data.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Chat extends Fragment {
    TextView txtReceive;
    TextView txtSend;
    View rootView;
    ListView list;
    User usermsg;
    Socket socket;
    ArrayList arrayList;
    ArrayAdapter<String> adapter;
    Client myclient;

    String ipAddress = "192.168.42.18";
    int port = 8081;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList = new ArrayList<User>();
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,arrayList);
        usermsg = new User();

        //myclient = new Client(this.ipAddress,this.port);
        //myclient.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Button btn_send = (Button)rootView.findViewById(R.id.btn_send);
        txtSend = (TextView)rootView.findViewById(R.id.word_client);
        list = (ListView)rootView.findViewById(R.id.listView);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        String myMessage = txtSend.getText().toString();
                        User userMessage = new User();

                        userMessage.setIp(myMessage);
                        userMessage.setOperation("MESSAGE");
                        sendMessage(userMessage);
                    }
                }.start();
            }
        });
    }

    public void sendMessage(Object obj){
        try {
            socket=new Socket();
            socket.connect(new InetSocketAddress(this.ipAddress, this.port),2000);
            ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(obj);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}