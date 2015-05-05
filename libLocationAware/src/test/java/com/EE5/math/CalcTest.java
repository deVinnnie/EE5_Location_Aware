package com.EE5.math;

import com.EE5.BuildConfig;
import com.EE5.image_manipulation.PatternCoordinator;
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
        Calc calc = new Calc(10,640,480);
        PatternCoordinator pattern = new PatternCoordinator(
                new Point(320-50,240-50),
                new Point(320+50,240-50),
                new Point(320-50,240+50),
                new Point(320+50,240+50),
                0.00);
        Point3D point = calc.calculate(pattern);

        assertEquals(0.0, point.getX());
        assertEquals(0.0, point.getY());
    }
}
