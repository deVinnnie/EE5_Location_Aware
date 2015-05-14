package com.rauwch.anton.demo_jaws.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rauwch.anton.demo_jaws.handlers.GameStateManager;
import com.rauwch.anton.demo_jaws.main.MyGdxGame;


/**
 * Created by Anton on 17/03/2015.
 */
public abstract class GameState
{
    protected GameStateManager gsm;
    protected MyGdxGame game;

    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    protected GameState(GameStateManager gsm)
    {
        this.gsm = gsm;
        game = gsm.game();
        sb = game.getSb();
        cam = game.getCam();
        hudCam = game.getHudCam();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();
}
