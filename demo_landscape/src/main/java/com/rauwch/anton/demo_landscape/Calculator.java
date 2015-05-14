package com.rauwch.anton.demo_landscape;


import android.util.Log;

/**
 * Created by Anton on 24/04/2015.
 */
public class Calculator
{
    public double x1,x2,y1,y2;

    public double rotation;
    public double distance;
    public float xDiff;
    public float yDiff;
    public Calculator(){
        x2 = 0;
        y2 = 0;
    }

    public void calcDiff()
    {
        xDiff = (float) (x2 - x1);
        yDiff = (float)(y2 - y1);
        Log.d("diff"," xDiff: " + xDiff + " yDiff:" + yDiff );
    }

    /** calculate angle between two phones and add own rotation
     made with the arrow pointing in mind **/
    public double calcAngle()
    {
        double xDiff = x2 - x1;
        double yDiff = y2 - y1;

        double angle = Math.toDegrees(Math.atan2(yDiff, xDiff));
        angle = angle - rotation;
        return angle;
    }

}
