package com.example.siyao_app;
import com.EE5.math.Calc;
import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;
import com.EE5.util.Point3D;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends ActionBarActivity {
    private EditText et1;
    private Button bt1;
    private int number1,number2,number3,number4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivityForResult(new Intent("com.EE5.image_manipulation.ImageManipulationsActivity"), 0);
        et1 = (EditText)findViewById(R.id.number1);
        bt1 = (Button)findViewById(R.id.button_start);
      //  bt2 = (Button)findViewById(R.id.button_restart);
        bt1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {

                String str1 = et1.getText().toString();
                number1 = Integer.parseInt(str1);
                number2 = 2;
                number3 = number2 + number1;
                number4 = number1 * number2;
                //System.out.println(number3);

                Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
                Position otherPosition = new Position(0,0,0,0);

               // GlobalResources.getInstance().setData();

                for (Map.Entry<String, Position> entry : GlobalResources.getInstance().getDevices().getMap().entrySet()) {
                    otherPosition = entry.getValue();
                    break; //Only read the position of the first device.
                }

                double distance = Calc.getDistance(ownPosition,otherPosition);
               if(distance<20) {
                   display(number3);
               }
                else{
                   display(number4);
               }
                //Dostop(number4);
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
