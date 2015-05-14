package com.castoryan.game.android;


/**
 * Created by Anton on 24/04/2015.
 */
public class Calculator
{
    public double x1,x2,y1,y2;
    public double rotation;
    public double distance;

    public Calculator(){}

    public void calcDistance()
    {
        double xDiff = x2 - x1;
        double yDiff = y2 - y1;
        distance = Math.sqrt(xDiff*xDiff + yDiff*yDiff);
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
