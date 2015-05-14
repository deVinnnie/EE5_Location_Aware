package com.rauwch.anton.demo_jaws.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rauwch.anton.demo_jaws.handlers.GameStateManager;
import com.rauwch.anton.demo_jaws.handlers.Jukebox;


public class MyGdxGame implements ApplicationListener, InputProcessor
{
    public static final String TITLE = "Silly Hats";
    public static final int V_WIDTH = 640;
    public static final int V_HEIGHT = 480;
    public static final int SCALE = 1;

    public static final float STEP = 1/60f;
    private float accum;
    private Viewport viewport;
    private SpriteBatch sb;

    private OrthographicCamera cam;
    private OrthographicCamera hudCam;


    private GameStateManager gsm;

    @Override
    public void create()
    {
        Gdx.input.setInputProcessor(this);
        gsm = new GameStateManager(this);
        sb = new SpriteBatch();
        cam= new OrthographicCamera();
        hudCam = new OrthographicCamera();
        Jukebox.load("JawsSlow.wav", "slow");
        Jukebox.load("JawsMid.wav", "mid");
        Jukebox.load("JawsFast.wav", "fast");
        Jukebox.load("JawsClose.wav","faster");
        Jukebox.load("JawsExtreem.wav","extreem");
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public void render()
    {
        accum += Gdx.graphics.getDeltaTime();
        while(accum >= STEP)
        {
            accum-= STEP;
            gsm.update(STEP);
            gsm.render();
        }

        //Gdx.gl.glClearColor(1, 1, 1, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    @Override
    public void resize(int width, int height)
    {
    }



    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
    public SpriteBatch getSb() {
        return sb;
    }

    public void setSb(SpriteBatch sb) {
        this.sb = sb;
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public void setCam(OrthographicCamera cam) {
        this.cam = cam;
    }

    public OrthographicCamera getHudCam() {
        return hudCam;
    }

    public void setHudCam(OrthographicCamera hudCam) {
        this.hudCam = hudCam;
    }

    public GameStateManager getGsm() {
        return gsm;
    }

    public void setGsm(GameStateManager gsm) {
        this.gsm = gsm;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        Jukebox.stopAll();
        if(gsm.theState == 1)
        {
            gsm.setState(GameStateManager.SHARK);
        }
        else if(gsm.theState == 2)
        {
            gsm.setState(GameStateManager.BOAT);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}




