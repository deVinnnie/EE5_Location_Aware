package com.rauwch.anton.demo_landscape;

import android.util.Log;


import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Map;

public class Game extends ApplicationAdapter {
    private OrthographicCamera camera;
    SpriteBatch batch;
    Sprite sprite;
    Texture texture;
    Calculator calc;

    @Override
    public void create ()
    {
        camera = new OrthographicCamera(1280, 720);
        calc = new Calculator();
        batch = new SpriteBatch();
        texture = new Texture("waldo.jpg");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        sprite = new Sprite(texture);
        sprite.setScale(1, 1);
        sprite.setOriginCenter();
        //sprite.setPosition(-100,-100);
        sprite.setPosition(-sprite.getWidth() / 2, -sprite.getHeight() / 2);


        //Some code to use a 'fake', in other words simulated, pattern detection.
        //PatternDetector detector = GlobalResources.getInstance().getPatternDetector();
        //detector.setPatternDetectorAlgorithm(new PatternDetectorAlgorithmMock());
        /*PatternDetectorAlgorithmInterface inter = GlobalResources.getInstance().getPatternDetector().getPatternDetectorAlgorithm();
        Log.i("Test", inter.getClass().toString());*/
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        sprite.draw(batch);

        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();

        calc.x1 = ownPosition.getX();
        calc.y1 = ownPosition.getY();
        //Log.d("arrows","Own postion " + calc.x1 + " " + calc.y1 );
        //Iterate over other devices.
        calc.calcDiff();
        sprite.setPosition(-(sprite.getWidth() / 2 + calc.xDiff*100), -(sprite.getHeight() / 2 + calc.yDiff*100
        ));


        batch.end();
    }
}