package com.castoryan.game.android;

import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;
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
    Texture bus;
    Texture stop;
    Texture people;
    Calculator calc;
    BitmapFont font;

    @Override
    public void create ()
    {
        calc = new Calculator();
        calc.x1 = 0; calc.y1 = 0;
        //calc.x2 = -100; calc.y2 = -173 ;
        calc.rotation = 90;
        batch = new SpriteBatch();
        bus = new Texture("BUS.png");
        stop = new Texture("STOP.png");
        people = new Texture("people.png");
        sprite = new Sprite(people);
        sprite.setPosition(500, 500); //Î»ÖÃ
        sprite.setRotation(115);//Ðý×ª
//        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
//                Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);
//        sprite.setOriginCenter();
//        sprite.setScale(1,1);
//        font = new BitmapFont();

        //Some code to use a 'fake', in other words simulated, pattern detection.
        //PatternDetector detector = GlobalResources.getInstance().getPatternDetector();
        //detector.setPatternDetectorAlgorithm(new PatternDetectorAlgorithmMock());
        /*PatternDetectorAlgorithmInterface inter = GlobalResources.getInstance().getPatternDetector().getPatternDetectorAlgorithm();
        Log.i("Test", inter.getClass().toString());*/
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);

//        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
//        calc.x1 = ownPosition.getX();
//        calc.y1 = ownPosition.getY();
//
//        //Iterate over other devices.
//        for (Map.Entry<String, Position> entry : GlobalResources.getInstance().getDevices().getMap().entrySet()) {
//            calc.x2 = entry.getValue().getX();
//            calc.y2 = entry.getValue().getY();
//            break; //Only read the position of the first device.
//        }
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