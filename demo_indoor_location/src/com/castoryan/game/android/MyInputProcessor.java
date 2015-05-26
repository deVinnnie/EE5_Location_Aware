package com.castoryan.game.android;

import android.content.Intent;

import com.EE5.SetupActivity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by CastorYan on 5/14/2015.
 */
public class MyInputProcessor implements InputProcessor {
    Game game;

    public MyInputProcessor(Game game){
        this.game = game;
    }


    @Override
    public boolean keyDown (int keycode) {
        return true;
    }

    @Override
    public boolean keyUp (int keycode) {
        return true;
    }

    @Override
    public boolean keyTyped (char character) {
        return true;
    }

    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        System.out.println("touch down at x=" + Gdx.input.getX()
                + ", y=" + Gdx.input.getY());
        if((x > 0)&(x<100)&(y>1700)&(y<1800)){
            game.launcher.returnToSetupActivity();
        }
        return true;
    }

    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {

        return true;
    }

    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        return true;
    }

    @Override
    public boolean mouseMoved (int x, int y) {
        return true;
    }

    @Override
    public boolean scrolled (int amount) {
        return true;
    }
}
