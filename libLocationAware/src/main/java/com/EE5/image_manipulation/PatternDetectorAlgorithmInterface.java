package com.EE5.image_manipulation;

import org.opencv.core.Mat;

public interface PatternDetectorAlgorithmInterface {

    /*
     * @param rgba Color image
     * @param gray2 Grayscale image
     * @return Corner points and angle of the pattern.
     */
    public PatternCoordinator find(Mat rgba, Mat gray2);
    public int getDistance();
}
