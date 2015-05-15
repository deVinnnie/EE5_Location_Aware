package com.EE5.image_manipulation;

import org.opencv.core.Point;

import java.io.Serializable;

/**
 * Holds the 4 coordinates of the corners of the pattern, and the rotation.
 */
public class PatternCoordinator implements Serializable{
    private static final long serialVersionUID = 2L;

    Point num1;
    Point num2;
    Point num3;
    Point num4;

    /**
     * Angle (orientation) in degrees.
     */
    double angle;

    public PatternCoordinator(Point num1,Point num2,Point num3,Point num4,double angle){
        this.num1=num1;
        this.num2=num2;
        this.num3=num3;
        this.num4=num4;
        this.angle=angle;
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

    public double getAngle() {
        return angle;
    }

    public void setNum1(Point num1) {
        this.num1 = num1;
    }

    public void setNum2(Point num2) {
        this.num2 = num2;
    }

    public void setNum3(Point num3) {
        this.num3 = num3;
    }

    public void setNum4(Point num4) {
        this.num4 = num4;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public String toString(){
        String format = "(%.2f, %.2f) (%.2f, %.2f) (%.2f, %.2f) (%.2f, %.2f) (rot:%.2f)";
        String string = String.format(format,
                this.getNum1().x, this.getNum1().y,
                this.getNum2().x, this.getNum2().y,
                this.getNum3().x, this.getNum3().y,
                this.getNum4().x, this.getNum4().y,
                this.getAngle());
        return string;
    }
}
