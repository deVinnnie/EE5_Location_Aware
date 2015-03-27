package com.EE5.image_manipulation;

import org.opencv.core.Point;

/**
 * Created by CastorYan on 3/14/2015.
 */
public class PatternCoordinator {
    Point num1;
    Point num2;
    Point num3;
    Point num4;
    public PatternCoordinator(Point num1,Point num2,Point num3,Point num4){
        this.num1=num1;
        this.num2=num2;
        this.num3=num3;
        this.num4=num4;
    }

    public Point getNum1() {
        return num1;
    }

    public Point getNum2() {
        return num2;
    }

    public Point getNum3() {
        return num3;
    }

    public Point getNum4() {
        return num4;
    }
}
