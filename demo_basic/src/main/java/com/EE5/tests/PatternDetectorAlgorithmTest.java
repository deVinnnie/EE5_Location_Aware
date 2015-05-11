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

public class PatternDetectorAlgorithmTest extends InstrumentationTestCase {

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

        assertEquals(177.0,pc.getNum1().x,5.0);
        assertEquals(376.0,pc.getNum1().y,5.0);

        assertEquals(177.0,pc.getNum2().x,5.0);
        assertEquals(314.0,pc.getNum2().y,5.0);

        assertEquals(239.0, pc.getNum3().x, 5.0);
        assertEquals(314.0, pc.getNum3().y, 5.0);

        assertEquals(239.0,pc.getNum4().x,5.0);
        assertEquals(376.0,pc.getNum4().y,5.0);

        assertEquals(180.0, pc.getAngle(), 2.0);
    }

    @SmallTest
    public void testWithImage_2() {
        this.makeTestImage(R.drawable.pattert_test_2);
        this.runAlgorithm();

        assertEquals(532.0,pc.getNum1().x,5.0);
        assertEquals(108.0,pc.getNum1().y,5.0);

        assertEquals(532.0,pc.getNum2().x,5.0);
        assertEquals(48.0,pc.getNum2().y,5.0);

        assertEquals(592.0,pc.getNum3().x,5.0);
        assertEquals(48.0,pc.getNum3().y,5.0);

        assertEquals(592.0,pc.getNum4().x,5.0);
        assertEquals(108.0,pc.getNum4().y,5.0);

        assertEquals(180.0, pc.getAngle(), 2.0);
    }

    @SmallTest
    public void testWithImage_3(){
        this.makeTestImage(R.drawable.pattert_test_3);
        this.runAlgorithm();

        assertEquals(221.0,pc.getNum1().x,5.0);
        assertEquals(139.0,pc.getNum1().y,5.0);

        assertEquals(196.0,pc.getNum2().x,5.0);
        assertEquals(83.0,pc.getNum2().y,5.0);

        assertEquals(252.0,pc.getNum3().x,5.0);
        assertEquals(58.0,pc.getNum3().y,5.0);

        assertEquals(276.0,pc.getNum4().x,5.0);
        assertEquals(115.0,pc.getNum4().y,5.0);

        ///assertEquals(, pc.getAngle(), 2.0);
    }

    @SmallTest
    public void testWithImage_4(){
        this.makeTestImage(R.drawable.pattert_test_4);
        this.runAlgorithm();

        assertEquals(245.0, pc.getNum1().x, 5.0);
        assertEquals(248.0,pc.getNum1().y,5.0);

        assertEquals(182.0,pc.getNum2().x,5.0);
        assertEquals(248.0,pc.getNum2().y,5.0);

        assertEquals(182.0,pc.getNum3().x,5.0);
        assertEquals(186.0,pc.getNum3().y,5.0);

        assertEquals(245.0,pc.getNum4().x,5.0);
        assertEquals(186.0,pc.getNum4().y,5.0);

        assertEquals(90.0, pc.getAngle(), 2.0);
    }

    @SmallTest
    public void testWithImage_5(){
        this.makeTestImage(R.drawable.pattert_test_5);
        this.runAlgorithm();

        assertEquals(244.0,pc.getNum1().x,5.0);
        assertEquals(187.0,pc.getNum1().y,5.0);

        assertEquals(245.0,pc.getNum2().x,5.0);
        assertEquals(247.0,pc.getNum2().y,5.0);

        assertEquals(185.0,pc.getNum3().x,5.0);
        assertEquals(247.0,pc.getNum3().y,5.0);

        assertEquals(185.0,pc.getNum4().x,5.0);
        assertEquals(187.0,pc.getNum4().y,5.0);

        double angle = pc.getAngle() % 360;
        assertEquals(0.0, angle, 2.0);
    }

    @SmallTest
    public void testWithImage_6(){
        this.makeTestImage(R.drawable.pattert_test_6);
        this.runAlgorithm();

        assertEquals(183.0,pc.getNum1().x,5.0);
        assertEquals(186.0,pc.getNum1().y,5.0);

        assertEquals(243.0,pc.getNum2().x,5.0);
        assertEquals(186.0,pc.getNum2().y,5.0);

        assertEquals(244.0,pc.getNum3().x,5.0);
        assertEquals(247.0,pc.getNum3().y,5.0);

        assertEquals(183.0,pc.getNum4().x,5.0);
        assertEquals(247.0,pc.getNum4().y,5.0);

        double angle = pc.getAngle();
        assertEquals(90, angle, 2.0);
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
