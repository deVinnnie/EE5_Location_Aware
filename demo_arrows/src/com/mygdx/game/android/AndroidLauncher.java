package com.mygdx.game.android;

import android.content.Intent;
import android.os.Bundle;

import com.EE5.SetupActivity;
import com.EE5.image_manipulation.PatternDetector;
import com.EE5.util.GlobalResources;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Start new Activity and block until it returns.
        startActivityForResult(new Intent(getApplicationContext(), com.EE5.SetupActivity.class),0);
        //----------------------------------------------

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        Game game = new Game();
        game.setLauncher(this);
        initialize(game, config);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*PatternDetector patternDetector = GlobalResources.getInstance().getPatternDetector();
        if(patternDetector != null) {
            patternDetector.destroy();
        }*/
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

    public void returnToSetupActivity(){
        Intent intent = new Intent(this, SetupActivity.class);
        startActivity(intent);
    }
}
