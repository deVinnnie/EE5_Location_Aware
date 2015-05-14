package com.rauwch.anton.demo_jaws.handlers;


import android.util.Log;

/**
 * Created by Anton on 24/04/2015.
 */
public class Calculator
{
    public double x1 = 0,x2 = 0,y1 = 0,y2 = 0;
    public double distance = 0;

    public Calculator(){}

    public void calcDistance()
    {
        Log.d("distance","calculating distance" + "x1: " + x1 + " y1: " + y1 + " x2: " + x2 + " y2: " + y2 );
        double xDiff = x2 - x1;
        double yDiff = y2 - y1;
        distance = Math.sqrt(xDiff*xDiff + yDiff*yDiff);
    }

}
