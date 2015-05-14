package com.rauwch.anton.demo_jaws.states;

import com.EE5.util.GlobalResources;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rauwch.anton.demo_jaws.R;
import com.rauwch.anton.demo_jaws.handlers.GameStateManager;


/**
 * Created by Anton on 27/03/2015.
 */
public class Boat extends GameState
{
    private Sprite sprite;
    private Viewport viewport;
    private Texture texture;
    private SpriteBatch batch;

    private OrthographicCamera b2dCam;

    public Boat(GameStateManager gsm)
    {
        super(gsm);
        batch = new SpriteBatch();
        texture = new Texture("boat.jpg");
        sprite = new Sprite(texture);
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - sprite.getHeight() / 2);
        sprite.setScale(2, 2);
        GlobalResources.getInstance().setData("Hello World");
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
        Gdx.gl.glClearColor(100 / 255f, 150 / 255f, 199 / 255f, 1);

        batch.begin();
        sprite.draw(batch);
        batch.end();

    }
    public void dispose()
    {
        batch.dispose();
        texture.dispose();
    }
}
