package com.EE5.math;

import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.server.data.Position;
import com.EE5.util.Point2D;
import com.EE5.util.Point3D;
import com.EE5.util.Vector;

import org.opencv.core.Point;

/**
 * Computes the x,y,z coordinates of the device using the outline of the detected pattern.
 */
public class Calc {
    private boolean convertToRealValues = true;

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
     *  Be careful with the width and height parameters: The height value (typically 640px) is bigger than the width value (typically 480px).
     *  This is unusual since photographs are (or at least should be) taken in landscape mode.
     *
     *  @param size The real life size of the pattern in cm (The length of one of the sides of the square)
     *             Size is in fact a misleading name, as it could be misinterpreted as the area of the square.
     *
     *  @param width Width in pixels of the full image in portrait.
     *  @param height Height in pixels of the full image in portrait.
     */
     public Calc(double size, double width, double height) {
         this(); //Call Default Constructor.
         /** the height value is 640 and the width value is 480
             the height is bigger then the width**/
         this.size = size;

         //this.xB = width;
         this.xB = 480;
         //this.yC = height;
         this.yC = 640;
         //this.xD = width;
         this.xD = 480;
         //this.yD = height;
         this.yD = 640;
         this.xA = 0;
         this.ya = 0;
         this.xC = 0;
         this.yB = 0;
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

        this.xB = 480;
        this.yC = 640;
        this.xD = 480;
        this.yD = 640;
        this.xA = 0;
        this.xC = 0;
        this.yB = 0;


        this.xa = pattern.getNum1().x;
        this.ya = pattern.getNum1().y;
        this.xb = pattern.getNum2().x;
        this.yb = pattern.getNum2().y;
        this.xc = pattern.getNum3().x;
        this.yc = pattern.getNum3().y;
        this.xd = pattern.getNum4().x;
        this.yd = pattern.getNum4().y;
        double angle = pattern.getAngle();

        //Start with calculations.

        /**
         * Angle of view in radians for the y-direction or x-direction???
         * Values based on real life measurements.
         */
        double phix= Math.toRadians(40.876);

        /**
         * Angle of view in radians for the y-direction or x-direction???
         * Values based on real life measurements.
         */
        double phiy= Math.toRadians(52.999*Math.PI);

        //Alpha
        // (z/x) * 480
        double canvasXSize = Math.abs(xA-xB);
        double fovx= (this.size / (Math.sqrt(Math.pow(xa-xb,2)+Math.pow(ya-yb,2)))) * canvasXSize;

        //Beta
        // (z/x) * 640
        double canvasYSize = Math.abs(yA-yC);
        double fovy= (this.size / (Math.sqrt(Math.pow(xa-xb,2)+Math.pow(ya-yb,2)))) * canvasYSize;


        //<editor-fold desc="Convert from Pixel Values to Real Values">
        PatternCoordinator realPatternCoordinator;
        if(convertToRealValues) {
            xa *= (fovx / 480.0);
            xb *= (fovx / 480.0);
            xc *= (fovx / 480.0);
            xd *= (fovx / 480.0);

            xA *= (fovx / 480.0);
            xB *= (fovx / 480.0);
            xC *= (fovx / 480.0);
            xD *= (fovx / 480.0);

            ya *= (fovy / 640.0);
            yb *= (fovy / 640.0);
            yc *= (fovy / 640.0);
            yd *= (fovy / 640.0);

            yA *= (fovy / 640.0);
            yB *= (fovy / 640.0);
            yC *= (fovy / 640.0);
            yD *= (fovy / 640.0);

           realPatternCoordinator = new PatternCoordinator(
                    new Point(pattern.getNum1().x * (fovx / 480.0), pattern.getNum1().y * (fovy / 640.0)),
                    new Point(pattern.getNum2().x * (fovx / 480.0), pattern.getNum2().y * (fovy / 640.0)),
                    new Point(pattern.getNum3().x * (fovx / 480.0), pattern.getNum3().y * (fovy / 640.0)),
                    new Point(pattern.getNum4().x * (fovx / 480.0), pattern.getNum4().y * (fovy / 640.0)),
                    pattern.getAngle()
            );
        }
        else{
            realPatternCoordinator = new PatternCoordinator(
                    new Point(pattern.getNum1().x, pattern.getNum1().y),
                    new Point(pattern.getNum2().x, pattern.getNum2().y),
                    new Point(pattern.getNum3().x, pattern.getNum3().y),
                    new Point(pattern.getNum4().x, pattern.getNum4().y),
                    pattern.getAngle()
            );
        }
        //</editor-fold>

        //Centerpoint of Pattern.
        double xe,ye;
        xe = (xa+xb+xc+xd)/4.0;
        ye = (ya+yb+yc+yd)/4.0;

        //Centerpoint of Image Canvas.
        double xE,yE;
        xE=(xA+xD)/2;
        yE=(yA+yD)/2;

        //Calculate height using field of view.
        Z1=1/((2*Math.tan(phix/2))/fovx); // 1/2 * tan(phix/2) / fovx;
        Z2=1/((2*Math.tan(phiy/2))/fovy);

        //Calculate the angle between the Pattern x-Axis and the point E (Centerpoint of the canvas)
        //Uses properties of vector for axis projection. (dot product in particular)

        //<editor-fold desc="Calculate X-coordinate">
        Point2D centre34 = this.getCentre(new Point2D(realPatternCoordinator.getNum3()), new Point2D(realPatternCoordinator.getNum4()));

        Vector xDirection = new Vector(
                centre34.getX() - xe,
                centre34.getY() - ye
        );
        xDirection.normalize();

        Vector toImageCenter = new Vector(
                xE - xe,
                yE - ye
        );

        double dotProductX = xDirection.dotProduct(toImageCenter);

        double cosAlphaX = 0;
        if(toImageCenter.getLength() != 0) {
            cosAlphaX = dotProductX / (xDirection.getLength() * toImageCenter.getLength());
        }
        X = cosAlphaX * toImageCenter.getLength();
        //</editor-fold>

        //<editor-fold desc="Calculate Y-coordinate">
        Point2D centre23 = this.getCentre(new Point2D(realPatternCoordinator.getNum2()), new Point2D(realPatternCoordinator.getNum3()));

        Vector yDirection = new Vector(
                centre23.getX() - xe,
                centre23.getY() - ye
        );
        yDirection.normalize();

        double dotProductY = yDirection.dotProduct(toImageCenter);
        double cosAlphaY = 0;
        if(toImageCenter.getLength() != 0) {
            cosAlphaY = dotProductY / (yDirection.getLength() * toImageCenter.getLength());
        }
        Y = cosAlphaY * toImageCenter.getLength();
        //</editor-fold>

        //Y=Math.sqrt(Math.pow(xe-xE,2)+Math.pow(ye-yE,2));
        //X=((Z*Math.sqrt(Math.pow(xa-xb,2)+Math.pow(ya-yb,2))*0.5)/distance)*(xb+xc-xB-xC)/(xB-xC); //the distance in X axis
        //Y=((Z*Math.sqrt(Math.pow(xa-xb,2)+Math.pow(ya-yb,2))*0.5)/distance)*(ya+yd-yA-yD)/(yA-yD);//the distance in Y axis

        double rate=fovy/fovx;
        double phiPict=Math.toDegrees(
                Math.atan(
                    (Math.abs(yc-yd))/(Math.abs(xc-xd))
                )
        );
        return new Point3D(X,Y,Z1);
    }

    public double getFieldOfView(){
        return 0;

    }

    public Point2D getCentre(Point2D point1, Point2D point2){
        return new Point2D(
                (point1.getX() + point2.getX()) / 2.0,
                (point1.getY() + point2.getY()) / 2.0
        );
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

    public boolean isConvertToRealValues() {
        return convertToRealValues;
    }

    public void setConvertToRealValues(boolean convertToRealValues) {
        this.convertToRealValues = convertToRealValues;
    }
}
