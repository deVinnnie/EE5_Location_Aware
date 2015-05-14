package com.castoryan.game.android;

import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by CastorYan on 5/14/2015.
 */
public class MyInputProcessor implements InputProcessor {


    public MyInputProcessor(){

    }


    @Override
    public boolean touchDown (int x, int y, int pointer, int button) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        return false;
    }


    @Override
    public boolean touchUp (int x, int y, int pointer, int button) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        return false;
    }



    @Override
    public boolean touchDragged (int x, int y, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y){
        return false;
    }

    @Override
     public boolean keyDown(int a){
        return false;
    }

    @Override
    public boolean keyUp(int a){
        return false;
    }

    @Override
    public boolean keyTyped(char a){
        return false;
    }

    @Override
    public  boolean scrolled(int x){
        Gdx.gl.glClearColor(1, 1, 0, 0);
        return false;
    }
}

