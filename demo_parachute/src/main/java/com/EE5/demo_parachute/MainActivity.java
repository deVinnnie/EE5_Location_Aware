package com.EE5.demo_parachute;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.EE5.SetupActivity;
import com.EE5.util.GlobalResources;

public class MainActivity extends Activity {
    private RelativeLayout.LayoutParams params;
    private RelativeLayout layout;
    private ImageView iv;
    private int y;

    private Handler loopHandler = new Handler();
    private Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {
            y++;
            if(y < layout.getHeight()) {
                MainActivity.this.params.topMargin=y;
            }
            else{
                MainActivity.this.params.topMargin = y =0;
            }
            layout.updateViewLayout(iv, params);

            GlobalResources.getInstance().setData(""+y);

            //Execute this code again after <sample_rate> milliseconds.
            loopHandler.postDelayed(loopRunnable, 50);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivityForResult(new Intent(getApplicationContext(), com.EE5.SetupActivity.class), 0);

        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.parachute_image);
        layout = (RelativeLayout) findViewById(R.id.parachute_layout);
        //layout.removeView(iv);
        params = new RelativeLayout.LayoutParams(300, 400);
        params.leftMargin = 0;
        params.topMargin = 0;
        params.bottomMargin = -400;
        //layout.addView(iv,params);
        layout.updateViewLayout(iv, params);

        loopHandler.postDelayed(loopRunnable, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Intent setupIntent = new Intent(this, SetupActivity.class);
        MenuItem setup = menu.findItem(R.id.action_setup);
        setup.setIntent(setupIntent);

        return true;
    }
}
