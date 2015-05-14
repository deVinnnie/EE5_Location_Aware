package com.rauwch.anton.demo_jaws.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


import java.util.HashMap;

/**
 * Created by Anton on 23/04/2015.
 */
public class Jukebox
{
    private static HashMap<String, Music> sounds;

    static
    {
        sounds = new HashMap<String, Music>();
    }

    public static void load(String path, String name)
    {
        Music sound = Gdx.audio.newMusic(Gdx.files.internal(path));
        sounds.put(name, sound);
    }
    public static void play(String name)
    {
        sounds.get(name).play();
        sounds.get(name).setLooping(true);
    }

    public  static void stop(String name)
    {
        sounds.get(name).stop();
    }

    public static void stopAll()
    {
        for(Music s : sounds.values())
        {
            s.stop();
        }
    }
}
