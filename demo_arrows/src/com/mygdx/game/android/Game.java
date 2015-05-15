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

import java.util.Map;

public class Game extends ApplicationAdapter {
    SpriteBatch batch;
    Sprite sprite;
    Texture img;
    Calculator calc;
    BitmapFont font;

    @Override
    public void create ()
    {
        calc = new Calculator();
        //calc.x1 = 0; calc.y1 = 0;
        //calc.x2 = 0; calc.y2 = 0;
        //calc.rotation = 90;
        batch = new SpriteBatch();
        img = new Texture("arrow.png");
        sprite = new Sprite(img);
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);
        sprite.setOriginCenter();
        sprite.setScale(1,1);
        font = new BitmapFont();

        //Some code to use a 'fake', in other words simulated, pattern detection.
        //PatternDetector detector = GlobalResources.getInstance().getPatternDetector();
        //detector.setPatternDetectorAlgorithm(new PatternDetectorAlgorithmMock());
        /*PatternDetectorAlgorithmInterface inter = GlobalResources.getInstance().getPatternDetector().getPatternDetectorAlgorithm();
        Log.i("Test", inter.getClass().toString());*/
        GlobalResources.getInstance().setData("Hello World");

        Position otherPosition = new Position(1.0,0.0,0.0,1.0);
        for (Map.Entry<String, Tuple<Position,String>> entry : GlobalResources.getInstance().getDevices().getAll()) {
            calc.x2 = entry.getValue().element1.getX();
            calc.y2 = entry.getValue().element1.getY();
            otherPosition = entry.getValue().element1;
            break; //Only read the position of the first device.
        }
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

        //TODO Debug rotation in SetupActivity. (y moves to the wrong side when turning device)

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
    }
}