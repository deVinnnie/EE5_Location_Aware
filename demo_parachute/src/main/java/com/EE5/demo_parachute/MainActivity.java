package com.EE5.demo_parachute;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startActivityForResult(new Intent("com.EE5.image_manipulation.ImageManipulationsActivity"), 0);
        setContentView(R.layout.activity_main);

        ImageView iv = (ImageView) findViewById(R.id.parachute_image);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.parachute_layout);
        layout.removeView(iv);
        layout.addView(iv,new RelativeLayout.LayoutParams(300, 400));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
