package com.example.david.demo_height_sound;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
/*import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;*/
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
//import android.widget.Button;

import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.image_manipulation.PatternDetector;
import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;


public class height_sound_app extends AppCompatActivity {


    /*public Context contexto;
   public MediaPlayer mp;*/
    protected MediaPlayer sound1;
    protected MediaPlayer sound2;
    protected MediaPlayer sound3;
    protected MediaPlayer sound4;
    protected MediaPlayer sound5;
    protected MediaPlayer sound6;
    protected MediaPlayer sound7;
    protected MediaPlayer sound8;
    public double height;
    private double beginheight;
    private boolean stopped=false;
    PatternCoordinator pattern;
   // private double prevheight;*/

    private Handler loopHandler = new Handler();
    private Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {

            Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();

            TextView myTextView = (TextView) findViewById(R.id.textView3);

            myTextView.setText("" + height);

           if (height <= (13*beginheight/20)) {


               sound1.setVolume(1,1);
               sound2.setVolume(0,0);
               sound3.setVolume(0,0);
               sound4.setVolume(0,0);
               sound5.setVolume(0,0);
               sound6.setVolume(0,0);
               sound7.setVolume(0,0);
               sound8.setVolume(0,0);


            } else if (height <= (14*beginheight/20)) {

               sound1.setVolume(0,0);
               sound2.setVolume(1,1);
               sound3.setVolume(0,0);
               sound4.setVolume(0,0);
               sound5.setVolume(0,0);
               sound6.setVolume(0,0);
               sound7.setVolume(0,0);
               sound8.setVolume(0,0);



           } else if (height <= (beginheight*15/20)) {

               sound1.setVolume(0,0);
               sound2.setVolume(0,0);
               sound3.setVolume(1,1);
               sound4.setVolume(0,0);
               sound5.setVolume(0,0);
               sound6.setVolume(0,0);
               sound7.setVolume(0,0);
               sound8.setVolume(0,0);


            } else if (height < (beginheight*16/20)) {


               sound1.setVolume(0,0);
               sound2.setVolume(0,0);
               sound3.setVolume(0,0);
               sound4.setVolume(1,1);
               sound5.setVolume(0,0);
               sound6.setVolume(0,0);
               sound7.setVolume(0,0);
               sound8.setVolume(0,0);



            } else if (height < (beginheight*17/20)) {


               sound1.setVolume(0,0);
               sound2.setVolume(0,0);
               sound3.setVolume(0,0);
               sound4.setVolume(0,0);
               sound5.setVolume(1,1);
               sound6.setVolume(0,0);
               sound7.setVolume(0,0);
               sound8.setVolume(0,0);



            } else if (height < (beginheight*18/20)) {


               sound1.setVolume(0,0);
               sound2.setVolume(0,0);
               sound3.setVolume(0,0);
               sound4.setVolume(0,0);
               sound5.setVolume(0,0);
               sound6.setVolume(1,1);
               sound7.setVolume(0,0);
               sound8.setVolume(0,0);


            } else if (height < (beginheight*19/20)) {


               sound1.setVolume(0,0);
               sound2.setVolume(0,0);
               sound3.setVolume(0,0);
               sound4.setVolume(0,0);
               sound5.setVolume(0,0);
               sound6.setVolume(0,0);
               sound7.setVolume(1,1);
               sound8.setVolume(0,0);


            } else {

               sound1.setVolume(0,0);
               sound2.setVolume(0,0);
               sound3.setVolume(0,0);
               sound4.setVolume(0,0);
               sound5.setVolume(0,0);
               sound6.setVolume(0,0);
               sound7.setVolume(0,0);
               sound8.setVolume(1,1);

            }

            //height++;
            height= ownPosition.getHeight();
            /*if(stopped){
                finish();

            }*/
            //Execute this code again after <sample_rate> milliseconds.
            loopHandler.postDelayed(loopRunnable, 200);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivityForResult(new Intent("com.EE5.image_manipulation.ImageManipulationsActivity"), 0);
        setContentView(R.layout.activity_height_sound_app);

        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
        beginheight= ownPosition.getHeight();
        /*final Button startbutton = (Button) findViewById(R.id.button2);
        final Button stopbutton = (Button) findViewById(R.id.button);*/
        sound1 = MediaPlayer.create(this, R.raw.sound1);
        sound2 = MediaPlayer.create(this, R.raw.sound2);
        sound3 = MediaPlayer.create(this, R.raw.sound3);
        sound4 = MediaPlayer.create(this, R.raw.sound4);
        sound5 = MediaPlayer.create(this, R.raw.sound5);
        sound6 = MediaPlayer.create(this, R.raw.sound6);
        sound7 = MediaPlayer.create(this, R.raw.sound7);
        sound8 = MediaPlayer.create(this, R.raw.sound8);
        sound1.setVolume(0,0);
        sound2.setVolume(0,0);
        sound3.setVolume(0,0);
        sound4.setVolume(0,0);
        sound5.setVolume(0,0);
        sound6.setVolume(0,0);
        sound7.setVolume(0,0);
        sound8.setVolume(0,0);
        TextView myTextView = (TextView) findViewById(R.id.textView6);

        myTextView.setText("" + beginheight);

        /*mp.setVolume(1,1);
        contexto=this;*/

        //height=30;
        sound1.start();
        sound1.setLooping(true);
        sound2.start();
        sound2.setLooping(true);
        sound3.start();
        sound3.setLooping(true);
        sound4.start();
        sound4.setLooping(true);
        sound5.start();
        sound5.setLooping(true);
        sound6.start();
        sound6.setLooping(true);
        sound7.start();
        sound7.setLooping(true);
        sound8.start();
        sound8.setLooping(true);

       /* startbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*long millis;
                millis = currentTimeMillis();*/
                    //stopped=false;
                //while (!stopped) {

                   // prevheight=height;

                    //height=160;

           // }
       // });*/


        /*stopbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopped=true;

                startbutton.setText("de app is nu gestopt!!!");
            }
        });

        startbutton.setEnabled(true);
        stopbutton.setEnabled(true);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_height_sound_app, menu);
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
    @Override
    protected void onDestroy() {
        loopHandler.removeCallbacks(loopRunnable);

        sound1.setLooping(false);
        sound2.setLooping(false);
        sound3.setLooping(false);
        sound4.setLooping(false);
        sound5.setLooping(false);
        sound6.setLooping(false);
        sound7.setLooping(false);
        sound8.setLooping(false);
        sound1.stop();
        sound2.stop();
        sound3.stop();
        sound4.stop();
        sound5.stop();
        sound6.stop();
        sound7.stop();
        sound8.stop();

        sound1.release();
        sound2.release();
        sound3.release();
        sound4.release();
        sound5.release();
        sound6.release();
        sound7.release();
        sound8.release();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PatternDetector patternDetector = GlobalResources.getInstance().getPatternDetector();
        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
        beginheight= ownPosition.getHeight();
        TextView myTextView = (TextView) findViewById(R.id.textView6);

        myTextView.setText("" + beginheight);
        if(patternDetector != null) {
            patternDetector.destroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PatternDetector patternDetector = GlobalResources.getInstance().getPatternDetector();
        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
        beginheight= ownPosition.getHeight();
        TextView myTextView = (TextView) findViewById(R.id.textView6);

        myTextView.setText("" + beginheight);
        if(patternDetector != null) {
            patternDetector.setup();
        }
    }
    public void START(View view) {
        // Do something in response to button

        loopHandler.post(loopRunnable);



        //SystemClock.sleep(500);


    }
    public void STOP(View view) {
        // Do something in response to button
        stopped=true;

        //mp.release();
        finish();


    }
}
