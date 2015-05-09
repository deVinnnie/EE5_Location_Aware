package com.mygdx.game.android;

import android.content.Intent;
import android.os.Bundle;

import com.EE5.image_manipulation.PatternDetector;
import com.EE5.util.GlobalResources;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Start new Activity and block until it returns.
        startActivityForResult(new Intent("com.EE5.image_manipulation.ImageManipulationsActivity"),0);
        //----------------------------------------------

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new Game(), config);
    }

    // For an overview of when and which events are called see:
    // http://www.tutorialspoint.com/android/android_acitivities.htm
    //<editor-fold desc="Android Activity Lifecycle Events">
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
    //</editor-fold>
}
