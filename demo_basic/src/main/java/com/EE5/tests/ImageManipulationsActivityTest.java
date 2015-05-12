package com.EE5.tests;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.EE5.image_manipulation.ImageManipulationsActivity;

import org.opencv.core.Mat;

public class ImageManipulationsActivityTest extends ActivityUnitTestCase<ImageManipulationsActivity> {
    private ImageManipulationsActivity activity;

    public ImageManipulationsActivityTest() {
        super(ImageManipulationsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Starts the MainActivity of the target application
        startActivity(new Intent(getInstrumentation().getTargetContext(), ImageManipulationsActivity.class), null, null);

        // Getting a reference to the MainActivity of the target application
        activity = getActivity();
        activity.onResume();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        this.activity.onPause();
    }

    @SmallTest
    public void testOpenCVReady(){
        boolean openCVReady = activity.OpenCVReady;
        assertTrue(openCVReady);
    }

    @SmallTest
    public void testWithImage(){
        Mat im = new Mat();
        //Highgui.imread("");
        //Bitmap bitmap = new Bitmap();

    }
}