package com.example.david.demo_height_sound;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.EE5.image_manipulation.PatternDetector;
import com.EE5.util.GlobalResources;


public class height_sound_app extends AppCompatActivity {

    protected MediaPlayer sound1;
    protected MediaPlayer sound2;
    protected MediaPlayer sound3;
    protected MediaPlayer sound4;
    protected MediaPlayer sound5;
    protected MediaPlayer sound6;
    protected MediaPlayer sound7;
    protected MediaPlayer sound8;
    public double height;
    private boolean stopped;
    private double prevheight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height_sound_app);
        startActivityForResult(new Intent("com.EE5.image_manipulation.ImageManipulationsActivity"), 0);
        final Button startbutton = (Button) findViewById(R.id.button2);
        final Button stopbutton = (Button) findViewById(R.id.button);
        sound1 = MediaPlayer.create(this, R.raw.sound1);
        sound2 = MediaPlayer.create(this, R.raw.sound2);
        sound3 = MediaPlayer.create(this, R.raw.sound3);
        sound4 = MediaPlayer.create(this, R.raw.sound4);
        sound5 = MediaPlayer.create(this, R.raw.sound5);
        sound6 = MediaPlayer.create(this, R.raw.sound6);
        sound7 = MediaPlayer.create(this, R.raw.sound7);
        sound8 = MediaPlayer.create(this, R.raw.sound8);
        sound1.setVolume(1,1);sound2.setVolume(1,1);sound3.setVolume(1,1);sound4.setVolume(1,1);
        sound5.setVolume(1,1);sound6.setVolume(1,1);sound7.setVolume(1,1);sound8.setVolume(1, 1);

        height=50;
        /*sound8.start();
        sound8.setLooping(true);*/

        startbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*long millis;
                millis = currentTimeMillis();*/
                    //stopped=false;
                //while (!stopped) {

                   // prevheight=height;
                    if (height < 50) {
                        sound1.start();
                        sound1.setLooping(true);


                    } else if (height < 70) {

                        sound2.start();
                        sound2.setLooping(true);


                    } else if (height < 90) {

                        sound3.start();
                        sound3.setLooping(true);


                    } else if (height < 110) {

                        sound4.start();
                        sound4.setLooping(true);


                    } else if (height < 130) {

                        sound5.start();
                        sound5.setLooping(true);


                    } else if (height < 150) {
                        sound6.start();
                        sound6.setLooping(true);


                    } else if (height < 170) {

                        sound7.start();
                        sound7.setLooping(true);


                    } else {

                        sound8.start();
                        sound8.setLooping(true);

                    }

                    //SystemClock.sleep(500);
                    if (height != prevheight) {
                        sound1.pause();
                        sound1.seekTo(0);
                        sound2.pause();
                        sound2.seekTo(0);
                        sound3.pause();
                        sound3.seekTo(0);
                        sound4.pause();
                        sound4.seekTo(0);
                        sound5.pause();
                        sound5.seekTo(0);
                        sound6.pause();
                        sound6.seekTo(0);
                        sound7.pause();
                        sound7.seekTo(0);
                        sound8.pause();
                        sound8.seekTo(0);
                    }
                    //height=160;
                }
           // }
        });


        stopbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopped=true;
                sound1.release();
                sound2.release();
                sound3.release();
                sound4.release();
                sound5.release();
                sound6.release();
                sound7.release();
                sound8.release();
                startbutton.setText("de app is nu gestopt!!!");
            }
        });

        startbutton.setEnabled(true);
        stopbutton.setEnabled(true);
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
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PatternDetector patternDetector = GlobalResources.getInstance().getPatternDetector();
        if(patternDetector != null) {
            patternDetector.destroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PatternDetector patternDetector = GlobalResources.getInstance().getPatternDetector();
        if(patternDetector != null) {
            patternDetector.setup();
        }
    }
}
