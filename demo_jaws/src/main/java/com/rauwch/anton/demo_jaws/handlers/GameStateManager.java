package com.rauwch.anton.demo_jaws.handlers;



import com.rauwch.anton.demo_jaws.main.MyGdxGame;
import com.rauwch.anton.demo_jaws.states.Boat;
import com.rauwch.anton.demo_jaws.states.GameState;
import com.rauwch.anton.demo_jaws.states.Shark;

import java.util.Stack;

/**
 * Created by Anton on 17/03/2015.
 */
public class GameStateManager {
    private MyGdxGame game;
    private Stack<GameState> gameStates;
    //public static int PLAY = 1;
    public static int BOAT = 1;
    public static int SHARK = 2;
    public int theState = 1;
    public GameStateManager(MyGdxGame game)
    {
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(BOAT);
    }

    public MyGdxGame game() {
        return game;
    }

    public void update(float dt)
    {
        gameStates.peek().update(dt);
    }

    public void render()
    {
        gameStates.peek().render();

    }

    private GameState getState(int state)
    {

        if(state == BOAT)
            return new Boat(this);
        else if(state == SHARK)
            return new Shark(this);
        return null;
    }

    public void setState(int state)
    {

        theState = state;
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        gameStates.push(getState(state));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }

    public void changeState()
    {
        GameState stat = gameStates.peek();
        if(stat == new Boat(this))
            setState(SHARK);
        else if(stat == new Shark(this))
            setState(BOAT);
    }


}


