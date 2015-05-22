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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

public class Game extends ApplicationAdapter {
    HashMap<String,Sprite> sp_list;
    SpriteBatch batch;
    Sprite sp_phone,sp_myphone;
    Texture background;
    Texture myphone;
    Texture phone;
    Calculator calc;
    BitmapFont font;
    double steplength;
    final double vof_length = 182;
    final double vof_width  = 134;
    @Override
    public void create () {
        sp_list = new HashMap<String, Sprite>();
        batch = new SpriteBatch();
        background = new Texture("axis.png");

        myphone = new Texture("myphone.png");
        phone = new Texture("p4013.png");


        sp_phone = new Sprite(phone);
        sp_myphone = new Sprite(myphone);
        sp_phone.setSize(Gdx.graphics.getHeight()*0.1f,Gdx.graphics.getWidth()*0.1f);
        sp_myphone.setSize(Gdx.graphics.getHeight()*0.1f,Gdx.graphics.getWidth()*0.1f);
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.setScale(2, 2);

        steplength = Gdx.graphics.getHeight()/vof_length;
    }

    @Override
    public void render () {

        int num_dev = GlobalResources.getInstance().getDevices().getAll().size();
        for(Map.Entry<String,Tuple<Position,String>> entry: GlobalResources.getInstance().getDevices().getAll()){
            String name = entry.getKey();
            Sprite sprite1 = new Sprite(phone);
            sp_list.put(name,sprite1);
        }

        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
        //DeviceList otherPosition = GlobalResources.getInstance().getDevices();
        //Position otherPosition = new Position(0.0,0.0,0.0,0.0);
        double length = Math.sqrt(Math.pow(ownPosition.getX(),2)+ Math.pow(ownPosition.getY(), 2));
        double degree = ownPosition.getRotation();
        Gdx.gl.glClearColor(1, 1, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        sp_myphone.setRotation(-(int) ownPosition.getRotation());
        sp_myphone.draw(batch);


        String st = "ownPosition is x=" + ownPosition.getX() +
                ", y = " + ownPosition.getY();
        String st2 ="device number is "+GlobalResources.getInstance().getDevices().getAll().size(); //"angle is " + ownPosition.getRotation()+"image width is"+Gdx.graphics.getWidth();
        font.draw(batch, st, 200, 200);
        font.draw(batch, st2, 200, 160);



        MyInputProcessor inputProcessor = new MyInputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);

        sp_myphone.setCenterX(Gdx.graphics.getWidth() / 2 + (int) (steplength * ownPosition.getX()));
        sp_myphone.setCenterY(Gdx.graphics.getHeight() / 2 + (int) (steplength * ownPosition.getY()));
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
        double pX2=0,pY2=0,pAngle=0;

         for (Map.Entry<String,Tuple<Position,String>> entry: GlobalResources.getInstance().getDevices().getAll()) {
            String name = entry.getKey();

             pX2 = entry.getValue().element1.getX();
             pY2 = entry.getValue().element1.getY();
             pAngle = entry.getValue().element1.getRotation();
             sp_phone = sp_list.get(name);
             sp_phone.setSize(Gdx.graphics.getHeight()*0.1f,Gdx.graphics.getWidth()*0.1f);
             sp_phone.setCenterX(Gdx.graphics.getWidth() / 2 + (int) (steplength * pX2));
             sp_phone.setCenterY(Gdx.graphics.getHeight() / 2 + (int) (steplength * pY2));
             sp_phone.setRotation( -(int)pAngle );
             sp_phone.draw(batch);
            //break; //Only read the position of the first device.
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