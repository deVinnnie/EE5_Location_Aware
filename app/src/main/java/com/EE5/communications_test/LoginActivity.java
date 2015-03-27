package com.EE5.communications_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.EE5.R;
import com.EE5.server.data.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class LoginActivity extends Activity {

    private User user =null;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final TextView tx_id = (TextView)findViewById(R.id.user_id_input);
        final TextView tx_password = (TextView)findViewById(R.id.user_password_input);
        Button btn_login = (Button)findViewById(R.id.btn_login);

        tx_id.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tx_id.setText("");
            }
        });
        tx_password.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                tx_password.setText("");
            }
        });

        user = new User();

        //WifiManager wm = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
        //String my_ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = tx_id.getText().toString();
                String password =tx_password.getText().toString();
                //isLogin = false;
                user.setUser_name(id);
                user.setPassword(password);
                user.setOperation("LOGIN");

                Thread loginThread = new Thread() {
                    @Override
                    public void run() {
                        isLogin = sendLoginInfo(user);
                    }
                };
                loginThread.start();

                try {
                    loginThread.join();
                }
                catch(Exception e){
                }

                if(isLogin == false) {
                    Toast.makeText(getApplicationContext(), "The b is " + isLogin, Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    Socket s;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    public boolean sendLoginInfo(Object obj){
        boolean result = false;
        try{
            s=new Socket();
            // s.connect(new InetSocketAddress("192.168.1.104", 8080));
            s.connect(new InetSocketAddress("192.168.42.18", 8081),2000);

            ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(obj);

            ObjectInputStream ois =new ObjectInputStream(s.getInputStream());
            result = ois.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
