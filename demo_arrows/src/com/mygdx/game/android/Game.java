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
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);

        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
        Position otherPosition = new Position(0.0,0.0,0.0,0.0);
        //calc.x1 = ownPosition.getX();//* Math.cos(Math.toRadians(ownPosition.getRotation()))- ownPosition.getY()*Math.sin(Math.toRadians(ownPosition.getRotation()));
        //calc.y1 = ownPosition.getY();//* Math.cos(Math.toRadians(ownPosition.getRotation()))+ ownPosition.getX()*Math.sin(Math.toRadians(ownPosition.getRotation()));
        //Log.d("arrows","Own postion " + calc.x1 + " " + calc.y1 );
        //Iterate over other devices.
        for (Map.Entry<String, Tuple<Position,String>> entry : GlobalResources.getInstance().getDevices().getAll()) {
            calc.x2 = entry.getValue().element1.getX();
            calc.y2 = entry.getValue().element1.getY();
            Log.i("T", entry.getValue().element2);
            break; //Only read the position of the first device.
        }

        Calc algorithmCalc = new Calc();
        Point2D dcPoint = algorithmCalc.convertToDeviceCentredCoordinates(otherPosition);

        double angle = -90; //Use correct offset to align with baseline.
        angle += Math.toDegrees(Math.atan2(dcPoint.getY(), dcPoint.getX()));

        sprite.setRotation( (float) 90);
        //batch.draw(img, 0, 0);

        //Draw Current Device Position.
        String position = "("+Math.round(ownPosition.getX()) + ", " + Math.round(ownPosition.getY())+")" +  " " + ownPosition.getRotation() + "°";
        font.draw(batch, position, 50, 50);

        batch.end();
    }
}