package com.example.siyao_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.EE5.math.Calc;
import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;
import com.EE5.util.Tuple;

import java.util.Map;


public class MainActivity extends Activity {
    private EditText et1;
    private Button bt1,bt2;
    private int number1,number3,number4;
    private int parsedNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivityForResult(new Intent(this, com.EE5.SetupActivity.class), 0);
        et1 = (EditText)findViewById(R.id.number1);

        bt1 = (Button)findViewById(R.id.button_start);

        bt2 = (Button)findViewById(R.id.button_send);

        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String str1 = et1.getText().toString();
                number1 = Integer.parseInt(str1);

                String number = "1";
                Position otherPosition = new Position();
                for (Map.Entry<String, Tuple<Position, String>> entry : GlobalResources.getInstance().getDevices().getAll()) {
                    number = entry.getValue().element2;
                    otherPosition = entry.getValue().element1;
                    break; //Only read the position of the first device.
                }
                parsedNumber = Integer.parseInt(number);

                Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();

                number3 = number1 + parsedNumber;
                number4 = number1 * parsedNumber;

                // double distance = Calc.getDistance(ownPosition,otherPosition);
                // int distance=15;
                double distance = Calc.getDistance(ownPosition, otherPosition);
                if (distance < 20) {
                    display(number3);
                } else {
                    display(number4);
                }
            }


        });

        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Send Number
                String str1 = et1.getText().toString();
                number1 = Integer.parseInt(str1);
                display(number1);
                GlobalResources.getInstance().setData(et1.getText().toString());
            }
        });
    }
    private void display(int number){
        Toast.makeText(this,""+number, 20).show();
    }
    @Override
    public boolean onCreateOptionsMenu ( Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
