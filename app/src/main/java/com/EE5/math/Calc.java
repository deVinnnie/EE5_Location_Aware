package com.EE5.math;

import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.util.Point3D;

public class Calc {
    double xa,xb,xc,xd,xA,xB,xC,xD,ya,yb,yc,yd,yA,yB,yC,yD;
    double distance;//the vertical dimension between cemeral and 2 dimension code
    double length;//the length that cemaral captured

    double wide;//the wide that cameral captured
    double size = 11.75;//the real size of 2 dimention code
    double X,Y,Z1,Z2,Z;
    double xe,ye,xE,yE;

    public Calc() {
    }

    public Calc(double size) {
        this.size = size;
        xA=0;yA=0;
        xB=480;yB=0;
        xC=0;yC=640;
        xD=480;yD=640;
    }

    public Point3D calculate(PatternCoordinator pattern){
        this.xa = pattern.getNum1().x;
        this.ya = pattern.getNum1().y;
        this.xb = pattern.getNum2().x;
        this.yb = pattern.getNum2().y;
        this.xc = pattern.getNum3().x;
        this.yc = pattern.getNum3().y;
        this.xd = pattern.getNum4().x;
        this.yd = pattern.getNum4().y;

        //Start with calculations.
        double phix=(40.876*Math.PI)/180;
        double phiy=(52.999*Math.PI)/180;
        double fovx=size/(Math.sqrt(Math.pow(xa-xb,2)+Math.pow(ya-yb,2)))*Math.abs(xA-xB);
        double fovy=size/(Math.sqrt(Math.pow(xa-xb,2)+Math.pow(ya-yb,2)))*Math.abs(yA-yC);
        /*double fovx1=size/(Math.sqrt(Math.pow(xa1-xb1,2)+Math.pow(ya1-yb1,2)))*Math.abs(xA-xB);
        double fovy1=size/(Math.sqrt(Math.pow(xa1-xb1,2)+Math.pow(ya1-yb1,2)))*Math.abs(yA-yC);*/
        /*distance=133; //unit is cm
        length=136.5; //unit is cm
        wide=98.5;  //unit is cm*/
        xe=(xa+xd)/2;
        ye=(ya+yd)/2;
        /* int xe1 = (xa1+xd1)/2;
        int ye1 = (ya1+yd1)/2;*/
        xE=(xA+xD)/2;
        yE=(yA+yD)/2;

        //Z=size/length*distance/Math.sqrt((Math.pow(xa-xb,2)+Math.pow(ya-yb,2))/(Math.pow(xA-xB,2)+Math.pow(yA-yB,2)));//the diatance in Z axis
        //Z2=size/wide*distance/Math.sqrt((Math.pow(xa-xc,2)+Math.pow(ya-yc,2))/(Math.pow(xA-xC,2)+Math.pow(yA-yC,2)));//the diatance in Z axis
        //Z=(Z1+Z2)/2;
        Z1=1/((2*Math.tan(phix/2))/fovx);
        Z2=1/((2*Math.tan(phiy/2))/fovy);
        X=fovx/480*(xe-xE);
        Y=fovy/640*(ye-yE);
        //Y=Math.sqrt(Math.pow(xe-xE,2)+Math.pow(ye-yE,2));
        //X=((Z*Math.sqrt(Math.pow(xa-xb,2)+Math.pow(ya-yb,2))*0.5)/distance)*(xb+xc-xB-xC)/(xB-xC); //the distance in X axis
        //Y=((Z*Math.sqrt(Math.pow(xa-xb,2)+Math.pow(ya-yb,2))*0.5)/distance)*(ya+yd-yA-yD)/(yA-yD);//the distance in Y axis
            double rate=fovy/fovx;
            double phipict=Math.atan((Math.abs(yc-yd))/(Math.abs(xc-xd)))*(180/Math.PI);

         /*       double testy = Math.abs(yd-yd1)*fovy/640;
                double testx = Math.abs(xd-xd1)*fovx/480;
        System.out.println("testy is: "+testy);
        System.out.println("testx is: "+testx);*/
        System.out.println("the coordinate in x direction is: "+X);
        System.out.println("the coordinate in y direction is: "+Y);
        System.out.println("the coordinate in z direction is: "+Z1);
        System.out.println("the coordinate in z2 direction is: "+Z2);
        System.out.println("the rotation of the picture is: "+phipict);
        System.out.println("the coordinate in fovx direction is: "+fovx);
        System.out.println("the coordinate in fovy direction is: "+fovy);
        System.out.println("rate is: " +rate);

        return new Point3D(X,Y,Z1);
    }
}
