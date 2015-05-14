package com.EE5.math;

import com.EE5.BuildConfig;
import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;
import com.EE5.util.Point2D;
import com.EE5.util.Point3D;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Point;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, emulateSdk = 21, manifest=Config.NONE)
public class CalcTest  {
    /**
     * Assert that when the pattern is in the dead centre of the image the coordinates are (0,0).
     * The pattern size is set to be 10cm in reality and 100 pixels wide in the image.
     */
    @Test
    public void testPatternInCentre(){
        Calc calc = new Calc(10,480,640);

        //Assume a pattern size in pixels of 50.
        //Simulate the contours around the center of the image 640x480
        PatternCoordinator pattern = new PatternCoordinator(
                new Point(240-50,320-50),
                new Point(240-50,320+50),
                new Point(240+50,320-50),
                new Point(240+50,320+50),
                0.00);
        Point3D point = calc.calculate(pattern);

        assertEquals(0.0, point.getX());
        assertEquals(0.0, point.getY());
    }

    //<editor-fold desc="Test pattern in image centre (x,y = 0,0) with rotation">
    //TODO: Set rotation using corner points.
    /*@Test
    public void testPatternInCentreWithRotation_45_Degrees(){
        Calc calc = new Calc(10,480,640);

        PatternCoordinator pattern = new PatternCoordinator(
                new Point(240-50,320-50),
                new Point(240-50,320+50),
                new Point(240+50,320-50),
                new Point(240+50,320+50),
                45.00);

        Point3D point = calc.calculate(pattern);
        assertEquals(0.0, point.getX());
        assertEquals(0.0, point.getY());
    }*/

    @Test
    public void testPatternInCentreWithRotation_90_Degrees(){
        Calc calc = new Calc(10,480,640);

        PatternCoordinator pattern = new PatternCoordinator(
                new Point(240-50,320+50),
                new Point(240+50,320-50),
                new Point(240+50,320+50),
                new Point(240-50,320-50),
                0.00);

        Point3D point = calc.calculate(pattern);
        assertEquals(0.0, point.getX());
        assertEquals(0.0, point.getY());
    }

    @Test
    public void testPatternInCentreWithRotation_180_Degrees(){
        Calc calc = new Calc(10,480,640);

        PatternCoordinator pattern = new PatternCoordinator(
                new Point(240+50,320-50),
                new Point(240+50,320+50),
                new Point(240-50,320-50),
                new Point(240-50,320+50),
                0.00);

        Point3D point = calc.calculate(pattern);
        assertEquals(0.0, point.getX());
        assertEquals(0.0, point.getY());
    }

    @Test
    public void testPatternInCentreWithRotation_270_Degrees(){
        Calc calc = new Calc(10,480,640);

        PatternCoordinator pattern = new PatternCoordinator(
                new Point(240+50,320+50),
                new Point(240-50,320-50),
                new Point(240-50,320+50),
                new Point(240+50,320-50),
                0.0);

        Point3D point = calc.calculate(pattern);
        assertEquals(0.0, point.getX());
        assertEquals(0.0, point.getY());
    }

    /*@Test
    public void testPatternInCentreWithRotation_315_Degrees(){
        Calc calc = new Calc(10,480,640);

        PatternCoordinator pattern = new PatternCoordinator(
                new Point(240-50,320-50),
                new Point(240-50,320+50),
                new Point(240+50,320-50),
                new Point(240+50,320+50),
                315.00);

        Point3D point = calc.calculate(pattern);
        assertEquals(0.0, point.getX());
        assertEquals(0.0, point.getY());
    }*/
    //</editor-fold>

    //<editor-fold desc="Test pattern off centre">
    @Test
    public void testPatternOffCentre_1(){
        Calc calc = new Calc(10,480,640);
        calc.setConvertToRealValues(false);

        PatternCoordinator pattern = new PatternCoordinator(
                new Point(20+5,20-5),
                new Point(20-5,20-5),
                new Point(20-5,20+5),
                new Point(20+5,20+5),
                0.00
        );

        Point3D point = calc.calculate(pattern);

        assertEquals(300.0, point.getX());
        assertEquals(-220.0, point.getY());
    }

    @Test
    public void testPatternOffCentre_2(){
        Calc calc = new Calc(10,480,640);
        calc.setConvertToRealValues(false);

        double halfSide = 14.14/2.0;
        double yCenter = 320.0-Math.sqrt(2);

        PatternCoordinator pattern = new PatternCoordinator(
                new Point(240,yCenter-halfSide),
                new Point(240-halfSide,yCenter),
                new Point(240,yCenter+halfSide),
                new Point(240+halfSide,yCenter),
                45.00
        );

        Point3D point = calc.calculate(pattern);

        assertEquals(1.0, point.getX(), 0.01);
        assertEquals(1.0, point.getY(), 0.01);
    }

    @Test
    public void testPatternOffCentre_3(){
        Calc calc = new Calc(10,480,640);
        calc.setConvertToRealValues(false);

        double halfSide = 14.14/2.0;
        double xCenter = 240.0+Math.sqrt(2);

        PatternCoordinator pattern = new PatternCoordinator(
                new Point(xCenter,320+halfSide),
                new Point(xCenter+halfSide,320),
                new Point(xCenter,320-halfSide),
                new Point(xCenter-halfSide,320),
                45.00
        );

        Point3D point = calc.calculate(pattern);

        assertEquals(1.0, point.getX(), 0.01);
        assertEquals(-1.0, point.getY(), 0.01);
    }
    //</editor-fold>

    //<editor-fold desc="Test Pattern off centre and rotation stationary">
    @Test
    public void testPatternOffCentreStationaryRot_1(){
        Calc calc = new Calc(10,480,640);

        //Calibration
        PatternCoordinator pattern = new PatternCoordinator(
                new Point(20+5,20-5),
                new Point(20-5,20-5),
                new Point(20-5,20+5),
                new Point(20+5,20+5),
                0.00
        );

        Point3D point = calc.calculate(pattern);
        double distance1 = Calc.getDistance(new Point3D(0,0,0), point);

        //Compare
        PatternCoordinator pattern2 = new PatternCoordinator(
                new Point(20+5,20+5),
                new Point(20+5,20-5),
                new Point(20-5,20-5),
                new Point(20-5,20+5),
                0.00
        );

        Point3D point2 = calc.calculate(pattern2);
        double distance2 = Calc.getDistance(new Point3D(0, 0, 0), point2);
        assertEquals(distance1, distance2);

        //Compare2
        PatternCoordinator pattern3 = new PatternCoordinator(
                new Point(20-5,20+5),
                new Point(20+5,20+5),
                new Point(20+5,20-5),
                new Point(20-5,20-5),
                0.00
        );

        Point3D point3 = calc.calculate(pattern3);
        double distance3 = Calc.getDistance(new Point3D(0, 0, 0), point3);
        assertEquals(distance1, distance3);

        //Compare3
        PatternCoordinator pattern4 = new PatternCoordinator(
                new Point(20-5,20-5),
                new Point(20-5,20+5),
                new Point(20+5,20+5),
                new Point(20+5,20-5),
                0.00
        );

        Point3D point4 = calc.calculate(pattern4);
        double distance4 = Calc.getDistance(new Point3D(0, 0, 0), point4);
        assertEquals(distance1, distance4);
    }

    //</editor-fold>

    //<editor-fold desc="Pattern Coordinate System to Device Centred Coordinate System">
    @Test
    public void testCoordinateTranslation_Not_Angled_1(){
        Calc calc = new Calc(10,480,640);
        Position ownPosition = new Position(
                20.0,
                20.0,
                0.0,
                10.0
        );

        Position otherPosition = new Position(
            -10.0,
            50,
            0.0,
            10.0
        );

        GlobalResources.getInstance().getDevice().setPosition(ownPosition);

        Point2D expectedPoint = new Point2D(30,-30);
        Point2D actualPoint = calc.convertToDeviceCentredCoordinates(otherPosition);

        assertEquals(expectedPoint.getX(),actualPoint.getX());
        assertEquals(expectedPoint.getY(),actualPoint.getY());
    }

    @Test
    public void testCoordinateTranslation_PointInPatternOrigin_and_Minus45Degrees(){
        Calc calc = new Calc(10,480,640);
        //Y direction = Line through Origin and OwnPosition.
        Position ownPosition = new Position(
                20.0,
                20.0,
                -45.0,
                10.0
        );

        Position otherPosition = new Position(
                0.0,0.0, 0.0, 0.0
        );

        GlobalResources.getInstance().getDevice().setPosition(ownPosition);

        double distance = Math.sqrt(20*20*2);
        Point2D expectedPoint = new Point2D(0.0, -distance);
        Point2D actualPoint = calc.convertToDeviceCentredCoordinates(otherPosition);

        assertEquals(expectedPoint.getX(),actualPoint.getX(), 0.001);
        assertEquals(expectedPoint.getY(),actualPoint.getY(), 0.001);
    }

    @Test
    public void testCoordinateTranslation_PointInPatternOrigin_and_45Degrees(){
        Calc calc = new Calc(10,480,640);
        //Y direction = Line through Origin and OwnPosition.
        Position ownPosition = new Position(
                20.0,
                -20.0,
                45.0,
                10.0
        );

        Position otherPosition = new Position(
                0.0,0.0, 0.0, 0.0
        );

        GlobalResources.getInstance().getDevice().setPosition(ownPosition);

        double distance = Math.sqrt(20*20*2);
        Point2D expectedPoint = new Point2D(0.0, -distance);
        Point2D actualPoint = calc.convertToDeviceCentredCoordinates(otherPosition);

        assertEquals(expectedPoint.getX(),actualPoint.getX(), 0.001);
        assertEquals(expectedPoint.getY(),actualPoint.getY(), 0.001);
    }

    @Test
    public void testCoordinateTranslation_Rotation_Larger_Than_90_Degrees(){
        Calc calc = new Calc(10,480,640);
        //Y direction = Line through Origin and OwnPosition.
        Position ownPosition = new Position(
                20.0,
                -20.0,
                135.0,
                10.0
        );

        Position otherPosition = new Position(
                10.0,10.0, 0.0, 0.0
        );

        GlobalResources.getInstance().getDevice().setPosition(ownPosition);

        Point2D expectedPoint = new Point2D(-28.5, -14.5);
        Point2D actualPoint = calc.convertToDeviceCentredCoordinates(otherPosition);

        assertEquals(expectedPoint.getX(),actualPoint.getX(), 0.5);
        assertEquals(expectedPoint.getY(),actualPoint.getY(), 0.5);
    }
    //</editor-fold>

    //<editor-fold desc="Other">
    @Test
    public void testGetDistance(){
        Point3D point1 = new Point3D(0,0,0);
        Point3D point2;
        double result;

        point2 = new Point3D(1,0,0);
        result = Calc.getDistance(point1, point2);
        assertEquals(1.0, result);

        point2 = new Point3D(0,1,0);
        result = Calc.getDistance(point1, point2);
        assertEquals(1.0, result);

        point2 = new Point3D(0,0,1);
        result = Calc.getDistance(point1, point2);
        assertEquals(1.0, result);
    }
    //</editor-fold>
}
