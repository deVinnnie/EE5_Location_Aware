package com.EE5.tests;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.EE5.basic_demo.R;
import com.EE5.image_manipulation.PatternCoordinator;
import com.EE5.image_manipulation.PatternDetectorAlgorithm;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class AlgorithmToCalcTest extends InstrumentationTestCase {

    private Mat testImage;
    private Mat testImageGray;
    private Bitmap testBitmap;
    private Resources resources;
    private PatternDetectorAlgorithm algo;
    private PatternCoordinator pc;

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        this.resources = getInstrumentation().getTargetContext().getResources();
        this.algo = new PatternDetectorAlgorithm();

        this.pc = new PatternCoordinator(
                new Point(0,0),
                new Point(0,0),
                new Point(0,0),
                new Point(0,0),
                0.0
        );
    }

    @SmallTest
    public void testWithImage_1(){
        this.makeTestImage(R.drawable.pattern_test_1);
        this.runAlgorithm();


    }

    public void makeTestImage(int id){
        Drawable dh = resources.getDrawable(id);
        this.testBitmap =((BitmapDrawable)dh).getBitmap();

        this.testImage = new Mat(testBitmap.getWidth(), testBitmap.getHeight(), CvType.CV_8SC3);
        this.testImageGray = new Mat(testBitmap.getWidth(), testBitmap.getHeight(), CvType.CV_8UC1);

        Utils.bitmapToMat(this.testBitmap, this.testImage);
        Imgproc.cvtColor(this.testImage, this.testImageGray, Imgproc.COLOR_RGB2GRAY);

        /*Utils.matToBitmap(testImage, testBitmap);
        Utils.matToBitmap(testImageGray, testBitmap);*/
    }

    public void runAlgorithm(){
        for(int i=0; i < 100;i++){
            pc = algo.find(testImage, testImageGray);
        }
    }
}
