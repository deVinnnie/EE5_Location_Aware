package com.example.david.demo_height_sound;

import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class height_sound_app extends ActionBarActivity {

    protected MediaPlayer sound1;
    protected MediaPlayer sound2;
    protected MediaPlayer sound3;
    protected MediaPlayer sound4;
    protected MediaPlayer sound5;
    protected MediaPlayer sound6;
    protected MediaPlayer sound7;
    protected MediaPlayer sound8;
    public double height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height_sound_app);
        final Button startbutton = (Button) findViewById(R.id.button);
        final Button stopbutton = (Button) findViewById(R.id.button2);
        sound1 = MediaPlayer.create(this, R.raw.sound1);
        sound2 = MediaPlayer.create(this, R.raw.sound2);
        sound3 = MediaPlayer.create(this, R.raw.sound3);
        sound4 = MediaPlayer.create(this, R.raw.sound4);
        sound5 = MediaPlayer.create(this, R.raw.sound5);
        sound6 = MediaPlayer.create(this, R.raw.sound6);
        sound7 = MediaPlayer.create(this, R.raw.sound7);
        sound8 = MediaPlayer.create(this, R.raw.sound8);
        sound1.setVolume(1,1);sound2.setVolume(1,1);sound3.setVolume(1,1);sound4.setVolume(1,1);
        sound5.setVolume(1,1);sound6.setVolume(1,1);sound7.setVolume(1,1);sound8.setVolume(1,1);

        startbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*long millis;
                millis = currentTimeMillis();*/

                if (height < 50) {
                    sound1.start();
                    sound1.setLooping(true);
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

                } else if (height < 70) {
                    sound1.pause();
                    sound1.seekTo(0);
                    sound2.start();
                    sound2.setLooping(true);
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

                } else if (height < 90) {
                    sound1.pause();
                    sound1.seekTo(0);
                    sound2.pause();
                    sound2.seekTo(0);
                    sound3.start();
                    sound3.setLooping(true);
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

                } else if (height < 110) {
                    sound1.pause();
                    sound1.seekTo(0);
                    sound2.pause();
                    sound2.seekTo(0);
                    sound3.pause();
                    sound3.seekTo(0);
                    sound4.start();
                    sound4.setLooping(true);
                    sound5.pause();
                    sound5.seekTo(0);
                    sound6.pause();
                    sound6.seekTo(0);
                    sound7.pause();
                    sound7.seekTo(0);
                    sound8.pause();
                    sound8.seekTo(0);

                } else if (height < 130) {
                    sound1.pause();
                    sound1.seekTo(0);
                    sound2.pause();
                    sound2.seekTo(0);
                    sound3.pause();
                    sound3.seekTo(0);
                    sound4.pause();
                    sound4.seekTo(0);
                    sound5.start();
                    sound5.setLooping(true);
                    sound6.pause();
                    sound6.seekTo(0);
                    sound7.pause();
                    sound7.seekTo(0);
                    sound8.pause();
                    sound8.seekTo(0);

                } else if (height < 150) {
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
                    sound6.start();
                    sound6.setLooping(true);
                    sound7.pause();
                    sound7.seekTo(0);
                    sound8.pause();
                    sound8.seekTo(0);

                } else if (height < 170) {
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
                    sound7.start();
                    sound7.setLooping(true);
                    sound8.pause();
                    sound8.seekTo(0);

                } else {
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
                    sound8.start();
                    sound8.setLooping(true);

                }
            }
        });


        stopbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sound1.stop();
                sound2.stop();
                sound3.stop();
                sound4.stop();
                sound5.stop();
                sound6.stop();
                sound7.stop();
                sound8.stop();
                startbutton.setText("er is gedrukt");
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
}
