package com.EE5.math;

import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.server.data.Position;
import com.EE5.util.Point3D;

/**
 * Computes the x,y,z coordinates of the device using the outline of the detected pattern.
 */
public class Calc {
    //Defines the corner points of the pattern.
    double xa,xb,xc,xd;
    double ya,yb,yc,yd;

    //Defines the corner points of the entire image.
    double xA,xB,xC,xD;
    double yA,yB,yC,yD;


    /**
     * The vertical distance between the camera and the pattern.
     */
    double distance;

    double length;//the length that cemara captured

    double wide;//the wide that camera captured

    /**
     * The real life size of the pattern in cm.
     */
    double size = 11.75;

    /**
     * Assumes a default image size of 640px x 480px
     */
    public Calc() {
        xA=0;yA=0;
        xB=640;yB=0;
        xC=0;yC=480;
        xD=640;yD=480;
    }

    /**
     *
     *  @param size The real life size of the pattern in cm (The length of one of the sides of the square)
     *             Size is in fact a misleading name, as it could be misinterpreted as the area of the square.
     *
     *  @param width Width in pixels of the full image.
     *  @param height Height in pixels of the full image.
     */
     public Calc(double size, double width, double height) {
         this(); //Call Default Constructor.

         this.size = size;

         this.xA = 0;
         this.ya = 0;
         this.xC = 0;
         this.yB = 0;
         this.xB = 640;
         this.yC = 480;
         this.xD = 640;
         this.yD = 480;
    }

    /**
     * Calculate the x,y and z coordinates of the device.
     * Position (0,0,0) is when the pattern is in the centre.
     *
     * @param pattern Corner points of the pattern.
     * @return A new point representing the position of the device.
     */
    public Point3D calculate(PatternCoordinator pattern){
        double X,Y,Z1,Z2,Z;
        double xe,ye,xE,yE;

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
        /*
        int xe1 = (xa1+xd1)/2;
        int ye1 = (ya1+yd1)/2;
        */
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


        /*
        double testy = Math.abs(yd-yd1)*fovy/640;
        double testx = Math.abs(xd-xd1)*fovx/480;
        System.out.println("testy is: "+testy);
        System.out.println("testx is: "+testx);
        */

        /*
        System.out.println("the coordinate in x direction is: "+X);
        System.out.println("the coordinate in y direction is: "+Y);
        System.out.println("the coordinate in z direction is: "+Z1);
        System.out.println("the coordinate in z2 direction is: "+Z2);
        System.out.println("the rotation of the picture is: "+phipict);
        System.out.println("the coordinate in fovx direction is: "+fovx);
        System.out.println("the coordinate in fovy direction is: "+fovy);
        System.out.println("rate is: " +rate);*/

        return new Point3D(X,Y,Z1);
    }

    /**
     * Calculates the distance between two positions.
     * Uses formula for Euclidean distance in 3 dimensions:
     * distance = √ ( (x2-x1)² + (y2-y1)² + (z2-z1)² )
     *
     * @param position1 First position
     * @param position2 Second Position
     * @return Distance between position1 and position2
     */
    public static double getDistance(Position position1, Position position2){
        double distance = Calc.getDistance(
                new Point3D(position1.getX(), position1.getY(), position1.getHeight()),
                new Point3D(position2.getX(), position2.getY(), position2.getHeight())

        );
        return distance;
    }

    /**
     * Calculates the distance between two points.
     * Uses formula for Euclidean distance in 3 dimensions:
     * distance = √ ( (x2-x1)² + (y2-y1)² + (z2-z1)² )
     *
     * @param point1 First point
     * @param point2 Second point
     * @return Distance between point1 and point2
     */
    public static double getDistance(Point3D point1, Point3D point2){
        double distance = Math.sqrt(
                Math.pow(
                        (point1.getX()-point2.getX()),2
                )
                +
                Math.pow(
                        (point1.getY()-point2.getY()),2
                )
                +
                Math.pow(
                        (point1.getZ()-point2.getZ()),2
                )
        );
        return distance;
    }

    public static double getDistance(double x1, double y1, double x2, double y2){
        double distance = Calc.getDistance(
                new Point3D(x1,y1,0),
                new Point3D(x2,y2,0)
        );
        return distance;
    }
}
