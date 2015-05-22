package com.EE5.math;

import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;
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
        /*xA=0;yA=0;
        xB=640;yB=0;
        xC=0;yC=480;
        xD=640;yD=480;*/
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

         this.xA = 0;
         this.yA = 0;

         this.xB = 480;
         this.yB = 0;

         this.xC = 0;
         this.yC = 640;

         this.xD = 480;
         this.yD = 640;
    }

    /**
     * Calculate the x,y and z coordinates of the device.
     * Position (0,0,0) is when the pattern is in the centre.
     *
     * @param pattern Corner points of the pattern.
     *                Num1 is the point at the white inner square.
     *                Moving clockwise the points should correspond to Num2, Num3 and Num4.
     *                X-axis is 480side, Y-axis is the 640side. Make sure the pattern is in the right coordinates!
     * @return A new point representing the position of the device.
     */
    public Point3D calculate(PatternCoordinator pattern){
        double X,Y,Z1,Z2;

        this.xB = 480;
        this.yC = 640;
        this.xD = 480;
        this.yD = 640;
        this.xA = 0;
        this.xC = 0;
        this.yB = 0;
        this.yA = 0;

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
         * Angle of view in radians for the x-direction.
         * Values based on real life measurements.
         */
        double phix= Math.toRadians(40.876);

        /**
         * Angle of view in radians for the y-direction.
         * Values based on real life measurements.
         */
        double phiy= Math.toRadians(52.999);

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

        //<editor-fold desc="Determine Centerpoint of Pattern.">
        double xe,ye;
        xe = (xa+xb+xc+xd)/4.0;
        ye = (ya+yb+yc+yd)/4.0;
        //</editor-fold>

        //<editor-fold desc="Determine Centerpoint of Image Canvas.">
        double xE,yE;
        xE=(xA+xD)/2;
        yE=(yA+yD)/2;
        //</editor-fold>

        //Calculate height using field of view of x.
        Z1=1/((2*Math.tan(phix/2))/fovx); // 1/2 * tan(phix/2) / fovx;

        //Calulate z using the field of view of y.
        //This should result in a higher accuracy as the y axis has higher resolution (640px).
        Z2=1/((2*Math.tan(phiy/2))/fovy);

        /*Use properties of vector for axis projection (dot product in particular) to transform the coordinates.
           The xDirection and yDirection are the base vectors for the axis of the pattern.
           The coordinates are relative to the screen (0,0) is the TopLeft corner of the image.
           The y-axis is the 640 side
           The x-axis is the 480 side.
           A scale factor may have been applied, but the principle remains the same.

        |    ---------------> y
        |     |-------|                              ^
      x |     |       |                              |  yDirection
        |     |o      |   ------ xDirection  ------> |
        |     |-------|
        `
         (xe,ye) centre of the pattern.
        */
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

        double rate=fovy/fovx;
        double phiPict=Math.toDegrees(
                Math.atan(
                    (Math.abs(yc-yd))/(Math.abs(xc-xd))
                )
        );

        //<editor-fold desc="Calculate Rotation (Baseline -> X-axis)">
        //Angle = Angle from baseline (y-as) of image to Pattern-based-X-axis.
        //Baseline below x-axis = positive angle.
        //x-axis above baseline = negative angle.
        Vector imageBaselineDirection = new Vector(
            0, 640
        );
        imageBaselineDirection.normalize();

        double dotProductBaseline = imageBaselineDirection.dotProduct(xDirection);

        //The crossproduct will be negative for negative angles, and positive for positive angles.
        double crossProduct = imageBaselineDirection.crossProduct(xDirection);

        double cosCorrectAngle = dotProductBaseline / (xDirection.getLength() * imageBaselineDirection.getLength());

        //The dotproduct always returns a positive angle. Using the crossproduct the sign of the angle is determined.
        double signAngle = Math.signum(crossProduct);
        if(crossProduct == 0){
            //If the crossProduct is zero, the signum function will return zero.
            //The resulting angle would always be zero, even if it was in fact 180 degrees. (cosine = -1)
            signAngle = 1;
        }
        double correctAngle = signAngle * Math.acos(cosCorrectAngle);
        pattern.setAngle(Math.toDegrees(correctAngle));
        //</editor-fold>

        return new Point3D(X,Y,Z2);
    }

    /**
     * Convert from "Pattern Coordinate System" to "Device Centred Coordinate System".
     * In the "Pattern Coordinate System" the origin is the center of the pattern.
     * X- and Y-axis are defined based on the inner rectangle.
     *
     * |-------|                              ^
     * |       |                              |  y-axis
     * |o      |   ------ x-axis  ------>     |
     * |-------|
     *
     *
     * In the "Device Centered Coordinate System" the origin is the position of the device.
     * The y-axis is parallel to the long side of the device (the 640px side) and points from the top of the device to the bottom.
     * The origin is however not linked to a position on the screen. This is only for getting the angles right!
     *
     * |-----------------------|
     * |                       |
     * |    ---  y-axis ---->  |
     * |                       |
     * |-----------------------|
     *
     *
     * @param position Other Position
     * @return position in Device Centred Coordinate System.
     */
    public Point2D convertToDeviceCentredCoordinates(Position position){
        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
        return this.convertToDeviceCentredCoordinates(ownPosition, position);
    }

    public Point2D convertToDeviceCentredCoordinates(Position ownPosition, Position position){
        double rotation = Math.toRadians(ownPosition.getRotation());

        //Careful with convention, yDevice is the baseline axis. xDevice is down.
        Vector xDeviceDirection = new Vector(
                Math.sin(rotation),
                Math.cos(rotation)
        );
        xDeviceDirection.normalize();

        Vector centreToPoint = new Vector(
            position.getX() - ownPosition.getX(),
            position.getY() - ownPosition.getY()
        );

        double dotProductX = xDeviceDirection.dotProduct(centreToPoint);
        double cosAlphaX = 0;
        if(centreToPoint.getLength() != 0) {
            cosAlphaX = dotProductX / (xDeviceDirection.getLength() * centreToPoint.getLength());
        }
        double X = cosAlphaX * centreToPoint.getLength();

        //Careful with convention, yDevice is the baseline axis. xDevice is down.
        Vector yDeviceDirection = new Vector(
                Math.cos(rotation),
                -Math.sin(rotation)
        );
        yDeviceDirection.normalize();

        double dotProductY = yDeviceDirection.dotProduct(centreToPoint);
        double cosAlphaY = 0;
        if(centreToPoint.getLength() != 0) {
            cosAlphaY = dotProductY / (yDeviceDirection.getLength() * centreToPoint.getLength());
        }
        double Y = cosAlphaY * centreToPoint.getLength();

        return new Point2D(X,Y);
    }

    /**
     * Calculates the centre between points. Shortcut for  [ 0.5 * (x1+x2) , 0.5 * (y1+y2) ]
     *
     * @param point1
     * @param point2
     * @return The centre between point1 and point2.
     */
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
