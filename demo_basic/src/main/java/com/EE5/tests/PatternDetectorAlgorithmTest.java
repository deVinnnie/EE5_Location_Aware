package com.EE5.tests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.EE5.basic_demo.R;
import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.image_manipulation.PatternDetectorAlgorithm;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class PatternDetectorAlgorithmTest extends ActivityTestCase {

    protected Mat sampleImage;
    protected Mat sampleImageGray;

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        Bitmap sampleBitmap = BitmapFactory.decodeResource(getInstrumentation().getContext().getResources(), R.drawable.sample_image);
        Utils.bitmapToMat(sampleBitmap,sampleImage);
        Imgproc.cvtColor(sampleImage, sampleImageGray, Imgproc.COLOR_RGB2GRAY);
    }

    @SmallTest
    public void testWithImage(){
        PatternDetectorAlgorithm algo = new PatternDetectorAlgorithm();
        PatternCoordinator pc = algo.find(sampleImage, sampleImageGray);
    }
}
