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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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
    Sprite sp_phone,sp_myphone,btn_return;
    Texture background;
    Texture myphone;
    Texture phone;
    Texture btn_re;
    Calculator calc;
    BitmapFont font;
    double steplength;
    double vof_length;
    double vof_width;
    Stage stage;

    MyLauncher launcher;

    public void setLauncher(MyLauncher launcher) {
        this.launcher = launcher;
    }


    @Override
    public void create () {

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Log.i("", "Down");
                launcher.returnToSetupActivity();
                return true;
            }
        });


        sp_list = new HashMap<String, Sprite>();
        batch = new SpriteBatch();
        background = new Texture("axis.png");

        myphone = new Texture("myphone.png");
        phone = new Texture("otherphone.png");
        btn_re = new Texture("btn_return.png");

        btn_return = new Sprite(btn_re);
        btn_return.setX(0);
        btn_return.setY(0);
        sp_phone = new Sprite(phone);
        sp_myphone = new Sprite(myphone);
        sp_phone.setSize(Gdx.graphics.getHeight()*0.1f,Gdx.graphics.getWidth()*0.1f);
        sp_myphone.setSize(Gdx.graphics.getHeight()*0.1f,Gdx.graphics.getWidth()*0.1f);
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.setScale(1, 1);


    }

    @Override
    public void render () {

        double heig = GlobalResources.getInstance().getDevice().getPosition().getHeight();
        double phiy= Math.toRadians(52.322);
        vof_length = heig * 2 * Math.tan(phiy/2);
        steplength = Gdx.graphics.getHeight()/vof_length;

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


        sp_myphone.setCenterX(Gdx.graphics.getWidth() / 2 + (int) (steplength * ownPosition.getX()));
        sp_myphone.setCenterY(Gdx.graphics.getHeight() / 2 + (int) (steplength * ownPosition.getY()));
        //sp_myphone.setCenterX(200 + (int) (steplength * ownPosition.getX()));
        //sp_myphone.setCenterY(200 + (int) (steplength * ownPosition.getY()));
        //sp_myphone.setRotation(-(int) ownPosition.getRotation());
        sp_myphone.draw(batch);
        btn_return.draw(batch);

        String st = "ownPosition is x=" + ownPosition.getX() +
                ", y = " + ownPosition.getY();
        String st2 ="vof_length is "+String.valueOf(vof_length);//GlobalResources.getInstance().getDevices().getAll().size(); //"angle is " + ownPosition.getRotation()+"image width is"+Gdx.graphics.getWidth();
        String st3 = "screenX =" + String.valueOf(200 + (int) (steplength * ownPosition.getX())) +
                ", screenY = " + String.valueOf(200 + (int) (steplength * ownPosition.getY()));
        font.draw(batch, st, 20, 200);
        font.draw(batch, st2, 20, 170);
        font.draw(batch, st3, 20, 140);

        MyInputProcessor inputProcessor = new MyInputProcessor(this);
        Gdx.input.setInputProcessor(inputProcessor);

         double pX2=0,pY2=0,pAngle=0;
         for (Map.Entry<String,Tuple<Position,String>> entry: GlobalResources.getInstance().getDevices().getAll()) {
            String name = entry.getKey();

             pX2 = entry.getValue().element1.getX();
             pY2 = entry.getValue().element1.getY();
             pAngle = entry.getValue().element1.getRotation();
             sp_phone = sp_list.get(name);
             sp_phone.setSize(Gdx.graphics.getHeight()*0.1f,Gdx.graphics.getWidth()*0.1f);
             sp_phone.setCenterX(200 + (int) (steplength * pX2));
             sp_phone.setCenterY(200 + (int) (steplength * pY2));
             //sp_phone.setRotation( -(int)pAngle );
             sp_phone.draw(batch);
            //break; //Only read the position of the first device.
        }

        batch.end();
        stage.draw();
    }

}