package com.mygdx.game.android;

import android.util.Log;

import com.EE5.math.Calc;
import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;
import com.EE5.util.Point2D;
import com.EE5.util.Tuple;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Map;

//import android.view.InputEvent;

public class Game extends ApplicationAdapter {
    Stage stage;
    SpriteBatch batch;
    Sprite sprite;
    Texture img;
    Calculator calc;
    BitmapFont font;
    TextButton button;
    TextureAtlas buttonAtlas;
    TextButton.TextButtonStyle textButtonStyle;

    AndroidLauncher launcher;

    public void setLauncher(AndroidLauncher launcher) {
        this.launcher = launcher;
    }

    @Override
    public void create ()
    {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Log.i("","Down");
                launcher.returnToSetupActivity();
                return true;
            }
        });

        calc = new Calculator();
        batch = new SpriteBatch();
        img = new Texture("arrow.png");
        sprite = new Sprite(img);
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);
        sprite.setOriginCenter();
        sprite.setScale(1, 1);
        font = new BitmapFont();
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);

        Position otherPosition = new Position(0.0,0.0,0.0,0.0);
        //Iterate over other devices.
        for (Map.Entry<String, Tuple<Position,String>> entry : GlobalResources.getInstance().getDevices().getAll()) {
            otherPosition = entry.getValue().element1;
            break; //Only read the position of the first device.
        }

        Calc algorithmCalc = new Calc();
        Point2D dcPoint = algorithmCalc.convertToDeviceCentredCoordinates(otherPosition);
        Log.i("dcPoint", "("+ dcPoint.getX() +"," + dcPoint.getY() +  ")");

        double correction = -90; //Use correct offset to align with baseline.
        double angle = Math.toDegrees(Math.atan2(dcPoint.getX(), dcPoint.getY()));
        Log.i("Angle", ""+angle);

        sprite.setRotation( (float) (correction+angle));
        //batch.draw(img, 0, 0);

        //Draw Current Device Position.
        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
        String position = "("+Math.round(ownPosition.getX()) + ", " + Math.round(ownPosition.getY())+")" +  " rot: " + ownPosition.getRotation() + "°";
        //String position = "("+Math.round(otherPosition.getX()) + ", " + Math.round(otherPosition.getY())+")" +  " " + otherPosition.getRotation() + "°";
        font.draw(batch, position, 50, 50);
        batch.end();
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}