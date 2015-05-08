package com.EE5.image_manipulation;

import org.opencv.core.Mat;
import org.opencv.core.Point;

/**
 * Dummy implementation of the Pattern Detection Algorithm.
 * It won't look at the camera output, but will return a predetermined set of values.
 * This is for testing purposes.
 */
public class PatternDetectorAlgorithmMock implements PatternDetectorAlgorithmInterface {

    private PatternCoordinator previousPattern = new PatternCoordinator(
            new Point(320-50,240-50),
            new Point(320+50,240-50),
            new Point(320-50,240+50),
            new Point(320+50,240+50),
            0.00
    );
    private MockMethod method = MockMethod.MOVING_UP_DOWN;

    public PatternDetectorAlgorithmMock(){
    }

    public PatternDetectorAlgorithmMock(MockMethod method){
        this.method = method;
    }

    /**
     * @param rgba Color image
     * @param gray2 Grayscale image
     * @return Corner points and angle of the pattern.
     */
    public PatternCoordinator find(Mat rgba, Mat gray2) {
        if(method.equals(MockMethod.STATIONARY)){
            return new PatternCoordinator(
                    new Point(320-50,240-50),
                    new Point(320+50,240-50),
                    new Point(320-50,240+50),
                    new Point(320+50,240+50),
                    0.00
            );
        }
        else{
            previousPattern.num1.x++;
            previousPattern.num2.x++;
            previousPattern.num3.x++;
            previousPattern.num4.x++;

            return previousPattern;
        }
    }

    public int getDistance(){
        return 0;
    }

    public void setDistance(int dis){
    }

    public enum MockMethod{
        STATIONARY,
        MOVING_UP_DOWN
    }
}