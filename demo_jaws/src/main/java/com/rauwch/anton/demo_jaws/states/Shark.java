package com.rauwch.anton.demo_jaws.states;

import android.util.Log;

import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;
import com.EE5.util.Tuple;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rauwch.anton.demo_jaws.handlers.Calculator;
import com.rauwch.anton.demo_jaws.handlers.GameStateManager;
import com.rauwch.anton.demo_jaws.handlers.Jukebox;


import java.util.Map;


/**
 * Created by Anton on 27/03/2015.
 */
public class Shark extends GameState
{
    private Calculator calc;
    private Sprite sprite;
    private Viewport viewport;
    private Texture texture;
    private SpriteBatch batch;

    AssetManager assetManager;
    private int state;


    private int prevState;

    private OrthographicCamera b2dCam;

    public Shark(GameStateManager gsm)
    {
        super(gsm);
        batch = new SpriteBatch();
        texture = new Texture("shark.jpg");
        calc = new Calculator();
        sprite = new Sprite(texture);
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);
        sprite.setScale(1,1);
    }

    public void handleInput()
    {

    }

    public void update(float dt)
    {

    }

    public void render()
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(100/255f, 150/255f, 199/255f, 1);
        //music.play();
        //music.setLooping(true);
        batch.begin();
        sprite.draw(batch);
        batch.end();

        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
        calc.x1 = ownPosition.getX();
        calc.y1 = ownPosition.getY();


        for (Map.Entry<String, Tuple<Position,String>> entry : GlobalResources.getInstance().getDevices().getAll()) {
            calc.x2 = entry.getValue().element1.getX();
            calc.y2 = entry.getValue().element1.getY();
            Log.i("T", entry.getValue().element2);
            break; //Only read the position of the first device.
        }
        calc.calcDistance();
        changeStat();
        if(prevState != state)
        {
            playSound();
            prevState = state;
        }


    }

    public void playSound()
    {
        Jukebox.stopAll();
        switch (state){
            case 0:
                Jukebox.stopAll();
                break;
            case 1:
               Jukebox.loop("slow");
                break;
            case 2:
                Jukebox.loop("mid");
                break;
            case 3:
                Jukebox.loop("fast");
                break;
            case 4:
                Jukebox.loop("faster");
                break;
            case 5:
                Jukebox.loop("extreem");
                break;

        }
    }
    public void dispose()
    {

        batch.dispose();
        texture.dispose();


    }

    private void changeStat()
    {
        Log.d("distance", " the distance is " + calc.distance);
        if(calc.distance > 100)
            state=0;
        else if(calc.distance > 50)
            state = 1;
        else if(calc.distance > 30)
            state = 2;
        else if(calc.distance >20)
            state = 3;
        else if(calc.distance >10)
            state = 4;
        else if(calc.distance >=0)
            state = 5;
        

    }
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPrevState() {
        return prevState;
    }

    public void setPrevState(int prevState) {
        this.prevState = prevState;
    }



}
