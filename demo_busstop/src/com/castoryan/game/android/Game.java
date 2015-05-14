package com.castoryan.game.android;

import android.provider.Contacts;
import android.util.Log;

import com.EE5.image_manipulation.PatternDetector;
import com.EE5.image_manipulation.PatternDetectorAlgorithm;
import com.EE5.image_manipulation.PatternDetectorAlgorithmInterface;
import com.EE5.image_manipulation.PatternDetectorAlgorithmMock;
import com.EE5.server.data.DeviceList;
import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;
import com.EE5.util.Tuple;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.castoryan.game.android.MyInputProcessor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

public class Game extends ApplicationAdapter {
    SpriteBatch batch;
    Sprite sprite;
    Texture bus;
    Texture stop;
    Texture people;
    Calculator calc;
    BitmapFont font;

    @Override
    public void create () {

        batch = new SpriteBatch();
        bus = new Texture("BUS.png");
        stop = new Texture("STOP.png");
        people = new Texture("people.png");
        sprite = new Sprite(people);
        sprite.setPosition(100, 100);
        sprite.setRotation(115);

        font = new BitmapFont();
        font.setColor(Color.RED);
        font.setScale(2,2);

    }

    @Override
    public void render () {

        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
        DeviceList otherPosition = GlobalResources.getInstance().getDevices();
        //Position otherPosition = new Position(0.0,0.0,0.0,0.0);

        Gdx.gl.glClearColor(1, 1, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.setRotation(-(int) ownPosition.getRotation());
        sprite.draw(batch);
        String st = "ownPosition is x=" + ownPosition.getX() +
                ", y = " + ownPosition.getY() + "angle is " + ownPosition.getRotation();
        font.draw(batch, st, 200, 200);



        MyInputProcessor inputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);

        sprite.setX(20 * (int) ownPosition.getY() + 400);
        sprite.setY(20*(int)ownPosition.getX()+700);

//        if(inputProcessor.touchDragged(xx,yy,zz)){
//            sprite.setX(Gdx.input.getX());
//            sprite.setY(900-Gdx.input.getY());
//        }

        calc = new Calculator();

//        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
//        calc.x1 = ownPosition.getX();
//        calc.y1 = ownPosition.getY();
//
//        //Iterate over other devices.
        //Iterator
        for (Map.Entry<String,Tuple<Position,String>> entry: GlobalResources.getInstance().getDevices().getAll()) {
            String name = entry.getKey();
            calc.x2 = entry.getValue().element1.getX();
            calc.y2 = entry.getValue().element1.getY();
            break; //Only read the position of the first device.
        }
//
//        calc.rotation = ownPosition.getRotation();
//        int angle = (int) calc.calcAngle();
//        sprite.setRotation(angle);
//        //batch.draw(img, 0, 0);
//
//        //Draw Current Device Position.
//        String position = "("+ownPosition.getX() + ", " + ownPosition.getY()+")";
//        font.draw(batch, position, 50, 50);

        batch.end();
    }

}