package com.EE5.image_manipulation;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.EE5.math.Calc;
import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;
import com.EE5.util.Point3D;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

/**
 * Grabs frames from the camera and gives them to the Pattern Detection Algorithm.
 * The results are stored in the {@link GlobalResources} Singleton.
 *
 * The constructor can be called at any time. The method {@link #setup()} has to be called after the OpenCV library has loaded.
 * Use {@link #destroy()} to clean up.
 *
 *
 * @see PatternDetectorAlgorithmInterface
 */
public class PatternDetector {
    public Mat image;

    private PatternDetectorAlgorithmInterface patternDetectorAlgorithm = new PatternDetectorAlgorithm();
    private PatternCoordinator patternCoordinates = new PatternCoordinator(
            new Point(320-50,240-50),
            new Point(320+50,240-50),
            new Point(320-50,240+50),
            new Point(320+50,240+50),
            0.00
    );

    private Handler handler;
    private long sampleRate = 50;
    private VideoCapture mCamera;

    private Calc calc = new Calc();

    private Handler cameraHandler = new Handler();
    private Runnable cameraRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (mCamera != null) {
                    Log.i("PatternDetector","Camera Frame Grabbed");
                    //Grab one frame of the camera.
                    boolean grabbed = mCamera.grab();
                    if (grabbed) {
                        Mat rgba = new Mat();
                        Mat gray = new Mat();
                        mCamera.retrieve(rgba, Highgui.CV_CAP_ANDROID_COLOR_FRAME_RGB);

                        if(camera == 1) {
                            //Flip the image around the openCv x-axis (== Calc y-axis) if the front facing camera is used.
                            //See: http://answers.opencv.org/question/8804/ipad-camera-input-is-rotated-180-degrees/
                            Core.flip(rgba, rgba, 1);
                        }

                        /*Bitmap bitmap = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(rgba, bitmap);*/

                        //Convert to grayscale.
                        Imgproc.cvtColor(rgba, gray, Imgproc.COLOR_RGB2GRAY);

                        //Quasi the same code as in onCameraFrame()
                        switch (PatternDetector.this.viewMode) {
                            case PatternDetector.VIEW_MODE_RGBA:
                                //Do no filtering and display the captured image unaltered on the screen.
                                break;

                            case PatternDetector.VIEW_MODE_CANNY:
                                //Apply the Canny Edge Detection and find the contours of the pattern.
                                patternCoordinates = patternDetectorAlgorithm.find(rgba, gray);
                                break;
                        }
                        PatternDetector.this.image = rgba;

                        PatternDetector.this.calculateCoordinates();
                        PatternDetector.this.alertify();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //Execute this code again after <sample_rate> milliseconds.
            cameraHandler.postDelayed(cameraRunnable, sampleRate);
        }
    };

    /**
     * Indicates which camera should be used.
     * '0' = back facing camera.
     * '1' = front facing camera.
     */
    private int camera = 1;

    public static final int VIEW_MODE_RGBA = 0;
    public static final int VIEW_MODE_CANNY = 2; //Canny edge detector
    private int viewMode = VIEW_MODE_RGBA;

    public PatternDetector() {
    }

    public PatternDetector(int camera) {
        this.setCamera(camera);
    }

    private void calculateCoordinates() {
        Point3D deviceCoordinates = calc.calculate(this.patternCoordinates); //Calculate the position of this device.
        Position devicePosition = new Position(deviceCoordinates.getX(), deviceCoordinates.getY(), this.patternCoordinates.getAngle(), deviceCoordinates.getZ());
        GlobalResources.getInstance().getDevice().setPosition(devicePosition);
    }

    /**
     * Load camera with the {@link VideoCapture} class.
     * Code is based on <a href="http://developer.sonymobile.com/knowledge-base/tutorials/android_tutorial/get-started-with-opencv-on-android/">http://developer.sonymobile.com/knowledge-base/tutorials/android_tutorial/get-started-with-opencv-on-android/</a>
     */
    public void setup() {
        Log.i("PatternDetector", "Setup Camera");
        if (mCamera != null) {
            //If a camera is already present release it first before setting up the new one.
            VideoCapture camera = mCamera;
            mCamera = null;
            camera.release();
        }

        mCamera = new VideoCapture(this.camera);

        cameraHandler.postDelayed(cameraRunnable, this.sampleRate);
    }

    /**
     * Release Camera and cleanup.
     */
    public void destroy(){
        Log.i("PatternDetector", "Release Camera");
        if(this.mCamera != null){
            this.mCamera.release();
            this.mCamera = null;
        }
        cameraHandler.removeCallbacks(cameraRunnable);
    }

    /**
     * Sends a signal to the UI Thread to update the user interface.
     * (Text overlay etc)
     */
    public void alertify(){
        Message msg = handler.obtainMessage();
        msg.what = 1;
        msg.obj = null;
        msg.arg1 = 0;
        handler.sendMessage(msg);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public Calc getCalc() {
        return calc;
    }

    public void setCalc(Calc calc) {
        this.calc = calc;
    }

    public int getCamera() {
        return camera;
    }

    private void setCamera(int camera) {
        if(camera != 0 && camera != 1){
            throw new IllegalArgumentException("Invalid value: camera should be 0 or 1.");
        }
        this.camera = camera;
    }

    public PatternDetectorAlgorithmInterface getPatternDetectorAlgorithm() {
        return patternDetectorAlgorithm;
    }

    public void setPatternDetectorAlgorithm(PatternDetectorAlgorithmInterface patternDetectorAlgorithm) {
        this.patternDetectorAlgorithm = patternDetectorAlgorithm;
    }

    public int getViewMode() {
        return viewMode;
    }

    public void setViewMode(int viewMode) {
        this.viewMode = viewMode;
    }
}