package com.EE5.demo_parachute;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
    private RelativeLayout.LayoutParams params;
    private RelativeLayout layout;
    private ImageView iv;

    private Handler loopHandler = new Handler();
    private Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {
            if(params.topMargin < layout.getHeight()) {
                MainActivity.this.params.topMargin+=4;
            }
            else{
                MainActivity.this.params.topMargin=0;
            }
            layout.updateViewLayout(iv, params);

            //Execute this code again after <sample_rate> milliseconds.
            loopHandler.postDelayed(loopRunnable, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startActivityForResult(new Intent("com.EE5.image_manipulation.ImageManipulationsActivity"), 0);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.parachute_image);
        layout = (RelativeLayout) findViewById(R.id.parachute_layout);
        //layout.removeView(iv);
        params = new RelativeLayout.LayoutParams(300, 400);
        params.leftMargin = 0;
        params.topMargin = 0;
        //layout.addView(iv,params);
        layout.updateViewLayout(iv, params);

        loopHandler.postDelayed(loopRunnable, 1000);
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
