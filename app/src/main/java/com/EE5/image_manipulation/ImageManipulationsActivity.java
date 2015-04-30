package com.EE5.image_manipulation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.EE5.R;
import com.EE5.client.IDGenerator;
import com.EE5.communications.ConnectionActivity;
import com.EE5.communications.ServerActivity;
import com.EE5.math.Calc;
import com.EE5.preferences.SettingsActivity;
import com.EE5.server.Server;
import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;
import com.EE5.util.Point3D;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ImageManipulationsActivity extends ActionBarActivity implements CvCameraViewListener2 {

    /**
     * Indicates which camera should be used.
     * '0' = back facing camera.
     * '1' = front facing camera.
     */
    private int camera = 1;

    public static final int VIEW_MODE_RGBA = 0;
    public static int viewMode = VIEW_MODE_RGBA;
    public static final int VIEW_MODE_CANNY = 2; //Canny edge detector

    private static final String TAG = "OCVSample::Activity";
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    private static final String TAG2 = "Running Time";

    TextView tx_x1;
    TextView tx_x2;
    TextView tx_y1;
    TextView tx_y2;
    TextView rotate;

    double ra;

    int distance;
    int distance2;
    boolean setupflag;

    ImageView iv;
    private MenuItem mItemPreviewRGBA;
    private MenuItem mItemPreviewCanny;
    private CameraBridgeViewBase mOpenCvCameraView;
    private Size mSize0;
    private Mat mIntermediateMat;
    private Mat mSepiaKernel;

    Scalar orange = new Scalar(255, 120, 0);
    Scalar light_blue = new Scalar(0, 255, 255);
    Scalar dark_blue = new Scalar(0, 120, 255);
    Scalar light_green = new Scalar(0, 255, 0);

    public static PatternCoordinator patternCoordinator = new PatternCoordinator(
            new Point(1,1),
            new Point(1,1),
            new Point(1,1),
            new Point(1,1),
            0.00
    );
    //Change later to non-static variable.

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        distance2 = 0;

        //Read out preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        double pattern_width = Double.parseDouble(sharedPref.getString("pattern_size", "11.75")); //Second param is default value.

        //Set device ID:
        String id = IDGenerator.generate(sharedPref);
        GlobalResources.getInstance().getDevice().setId(id);

        //Derive the image size.
        Camera camera=Camera.open();
        Camera.Parameters params = camera.getParameters();
        Camera.Size imageSize = params.getPictureSize();
        camera.release();
        Log.i("ImageSize", imageSize.width + " " +  imageSize.height);
        this.calc = new Calc(pattern_width, imageSize.width, imageSize.height);

        boolean backfacingCamera = sharedPref.getBoolean("camera", false);
        this.camera = (backfacingCamera) ? 0 : 1;

        if(Camera.getNumberOfCameras()<2){
            this.camera = 0;
            //Use rear facing camera if there is no front facing camera.
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.image_manipulations_surface_view);

        tx_x1 = (TextView) findViewById(R.id.x_axis);
        tx_x2 = (TextView) findViewById(R.id.x_axis2);
        tx_y1 = (TextView) findViewById(R.id.y_axis);
        tx_y2 = (TextView) findViewById(R.id.y_axis2);
        rotate = (TextView) findViewById(R.id.angle);

        iv = (ImageView) findViewById(R.id.imageView);
        /*iv.setVisibility(false);*/

        Button btn_get = (Button) findViewById(R.id.btn_get);
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotate.setText(String.valueOf(distance2));
                tx_y1.setText(String.valueOf(ra));
            }
        });

        SeekBar my_bar = (SeekBar) findViewById(R.id.my_bar);
        my_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distance = seekBar.getProgress();
                tx_y2.setText(String.valueOf(distance));
            }
        });

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.image_manipulations_activity_surface_view);
        mOpenCvCameraView.setCameraIndex(this.camera);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    //<editor-fold desc="Options">
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global, menu);

        Log.i(TAG, "called onCreateOptionsMenu");
        mItemPreviewRGBA  = menu.add("Preview RGBA");
        mItemPreviewCanny = menu.add("Canny");

        //Fill the option menu with Menu Items to other activities.
        //Each Item is defined with with an intent specifying the class name of the corresponding activity.
        //Clicks on a menu item are handled in the onOptionsItemsSelected method.

        //Link to the Preferences screen.
        Intent prefsIntent = new Intent(this, SettingsActivity.class);
        MenuItem preferences = menu.findItem(R.id.action_settings);
        preferences.setIntent(prefsIntent);

        //Option to start as server.
        Intent serverIntent = new Intent(this, ServerActivity.class);
        MenuItem useAsServer = menu.findItem(R.id.action_use_as_server);
        useAsServer.setIntent(serverIntent);

        //Option to start as client.
        Intent clientIntent = new Intent(this, ConnectionActivity.class);
        MenuItem useAsClient = menu.findItem(R.id.action_use_as_client);
        useAsClient.setIntent(clientIntent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

        //First check if Canny or RGBA was clicked.
        if (item == mItemPreviewRGBA) {
            viewMode = VIEW_MODE_RGBA;
            return true;
        }
        else if (item == mItemPreviewCanny) {
            viewMode = VIEW_MODE_CANNY;
            return true;
        }

        //Now check the other options...
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(item.getIntent());
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }
    //</editor-fold>

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                ImageManipulationsActivity.this.updateServerPositions();
            }
            super.handleMessage(msg);
        }
    };

    private Calc calc = new Calc();

    /**
     * Refreshes the overlay text using the latest positions.
     */
    public void updateServerPositions(){
        Point3D devicePosition = calc.calculate(ImageManipulationsActivity.patternCoordinator); //Calculate the position of this device.
        GlobalResources.getInstance().getDevice().setPosition(
                new Position(devicePosition.getX(), devicePosition.getY(), patternCoordinator.getAngle(), devicePosition.getZ())
        );

        Server server = GlobalResources.getInstance().getServer();

        //Only execute this code when running as server.
        if(server != null) {
            String list=""; //Temporary variable for building the text for the TextView overlay.

            //Iterate over all devices and save relevant information to TextView.
            for (Map.Entry<String, Position> entry : server.getConnectedDevices().getMap().entrySet()) {
                String id = entry.getKey();
                Position position = entry.getValue();

                /*list +="" + id + "\n(" + Math.round(pc.getNum1().x) + "," + Math.round(pc.getNum1().y) + ")  ("
                        + Math.round(pc.getNum2().x) + "," + Math.round(pc.getNum2().y) + ")  ("
                        + Math.round(pc.getNum3().x) + "," + Math.round(pc.getNum3().y) + ")  ("
                        + Math.round(pc.getNum4().x) + "," + Math.round(pc.getNum4().y) + ")\n";*/

                DecimalFormat df = new DecimalFormat("#.##"); //Round to two decimal places.

                list += "x:"+df.format(position.getX())+"; y:" + df.format(position.getY()) +"; z:" + df.format(position.getHeight()) + "; rot:" + df.format(position.getRotation());
                list+="\n";

                //Calculate the distance from the server to each of the devices.
                //Uses formula for Euclidean distance in 3 dimensions:
                // distance = √ ( (x2-x1)² + (y2-y1)² + (z2-z1)² )
                double distance = Math.sqrt(
                            Math.pow(
                                    (position.getX()-devicePosition.getX()),2
                            )
                            +
                            Math.pow(
                                    (position.getY()-devicePosition.getY()),2
                            )
                            +
                            Math.pow(
                                    (position.getHeight()-devicePosition.getZ()),2
                            )
                );

                list+="distance="+distance;
            }

            TextView server_display = (TextView) findViewById(R.id.server_display);
            server_display.setText(list);
        }
    }

    //<editor-fold desc="Algorithm & OpenCV">
    public void onCameraViewStarted(int width, int height) {
        mIntermediateMat = new Mat();
        mSize0 = new Size();

        // Fill sepia kernel
        mSepiaKernel = new Mat(4, 4, CvType.CV_32F);
        mSepiaKernel.put(0, 0, /* R */0.189f, 0.769f, 0.393f, 0f);
        mSepiaKernel.put(1, 0, /* G */0.168f, 0.686f, 0.349f, 0f);
        mSepiaKernel.put(2, 0, /* B */0.131f, 0.534f, 0.272f, 0f);
        mSepiaKernel.put(3, 0, /* A */0.000f, 0.000f, 0.000f, 1f);
    }

    public void onCameraViewStopped() {
        // Explicitly deallocate Mats
        if (mIntermediateMat != null)
            mIntermediateMat.release();

        mIntermediateMat = null;
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        //Size process_size = new Size(640,360);
        Mat rgba = inputFrame.rgba(); //Transforms frames from camera to rgba color type.
        Mat gray = inputFrame.gray();
        Size sizeRgba = rgba.size();

        switch (ImageManipulationsActivity.viewMode) {
            case ImageManipulationsActivity.VIEW_MODE_RGBA:
                //Do no filtering and display the captured image unaltered on the screen.
                break;

            case ImageManipulationsActivity.VIEW_MODE_CANNY:
                //Apply the Canny Edge Detection and find the contours of the pattern.
                PatternCoordinator pattern = patternDetection(rgba, gray);
                ImageManipulationsActivity.patternCoordinator = pattern;
                break;
        }

        //Imgproc.resize(rgba,rgba,new Size(352,288));

        //Give signal to update the text overlay.
        Message msg = handler.obtainMessage();
        msg.what = 1;
        msg.obj = null;
        msg.arg1 = 0;
        handler.sendMessage(msg);

        return rgba;
    }

    /**
     * Do magic!
     *
     * <img src="./doc-files/PatternDetection_01.png" alt="Pattern Detection 01"/>
     *
     * (Uses bitwise operators instead of logical operators. Bitwise is marginally faster.)
     *
     * @param rgba Color image
     * @param gray2 Grayscale image
     * @return Corner points and angle of the pattern. ???????????????????
     */
    public PatternCoordinator patternDetection(Mat rgba, Mat gray2) {
        double extra_angle = 0;

        long startTime = System.currentTimeMillis();
        //Imgproc.resize(gray2,gray2,process_size);

        List<MatOfPoint> contour = new ArrayList<MatOfPoint>();
        Mat hierarchys = new Mat();

        Imgproc.threshold(gray2, mIntermediateMat, 80, 255,Imgproc.THRESH_BINARY); //Apply tresholding, result is stored in 'mIntermediateMat'
        //Imgproc.threshold(gray2, mIntermediateMat, 80, 255, Imgproc.THRESH_OTSU);
        //Imgproc.Canny(mIntermediateMat, mIntermediateMat, 80, 90);                           //mIntermediateMat is a Mat format variable in field


        //Let OpenCV find contours, the result of this operation is stored in 'contour'.
        Imgproc.findContours(mIntermediateMat, contour, hierarchys, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        List<MatOfPoint> con_in_range;
        List<MatOfPoint> squareContours;
        List<MatOfPoint> pContour;

        MatOfPoint squ_in  = new MatOfPoint();
        MatOfPoint squ_out = new MatOfPoint();

        if(setupflag == false) {
            //Filter contours with the wrong size.
            con_in_range = getContoursBySize(distance2, contour);
            //Filter out non square contours.
            squareContours = getContoursSquare2(con_in_range);
            //Find the right contour for the pattern.
            pContour = findPattern(squareContours);
            if (pContour.size() == 2) {
                squ_out = pContour.get(1);
                squ_in = pContour.get(0);
                ra = Imgproc.contourArea(squ_out)/Imgproc.contourArea(squ_in);
                if((ra>3)&(ra<7)){
                    setupflag = true;
                    distance2 = distance2 + 5;
                }
                else{
                    setupflag = false;
                }
            }
            distance2 = distance2 + 5;
            PatternCoordinator pc = new PatternCoordinator(
                    new Point(1,1),
                    new Point(1,1),
                    new Point(1,1),
                    new Point(1,1),
                    0.00
            );
            return pc;
        }
        else {
            con_in_range = getContoursBySize(distance2, contour);
            squareContours = getContoursSquare2(con_in_range);
            pContour = findPattern(squareContours);

            Point innerCenter = new Point();
            Point outterCenter = new Point();

            RotatedRect NewMtx1 = new RotatedRect();
            RotatedRect NewMtx2 = new RotatedRect();

            MatOfPoint2f appo = new MatOfPoint2f();
            MatOfPoint2f appo2 = new MatOfPoint2f();
            if (pContour.size() == 2) {
                appo = new MatOfPoint2f(pContour.get(0).toArray());
                NewMtx1 = Imgproc.minAreaRect(appo);
                outterCenter = NewMtx1.center;

                appo2 = new MatOfPoint2f(pContour.get(1).toArray());
                NewMtx2 = Imgproc.minAreaRect(appo2);
                innerCenter = NewMtx2.center;
            }

            List<MatOfPoint> appro_con = new ArrayList<MatOfPoint>();
            appro_con.add(new MatOfPoint(appo.toArray()));

            Imgproc.cvtColor(mIntermediateMat, rgba, Imgproc.COLOR_GRAY2BGRA, 4);

            //Overlay the image with some useful lines.
            //Imgproc.drawContours(rgba, contour, -1, orange, 4);
            //Imgproc.drawContours(rgba,outterContours,-1,new Scalar(120,120,255),4);
            Imgproc.drawContours(rgba, squareContours, -1, orange, 4);
            //Original:Imgproc.drawContours(rgba, pContour, -1, light_green, 4);
            //Imgproc.drawContours(rgba, pContour, -1, light_green, 2);
            //Imgproc.drawContours(rgba, pContour, 0, light_green, 2);
            //Core.line(rgba, innerCenter, outterCenter, orange, 2);

            //Outer
            Point a = new Point(NewMtx2.boundingRect().x, NewMtx2.boundingRect().y);
            Point b = new Point(NewMtx2.boundingRect().x + NewMtx2.boundingRect().width, NewMtx2.boundingRect().y + NewMtx2.boundingRect().height);
            Core.rectangle(rgba, a, b, dark_blue,3);

            //Inner
            Point a2 = new Point(NewMtx1.boundingRect().x, NewMtx1.boundingRect().y);
            Point b2 = new Point(NewMtx1.boundingRect().x + NewMtx1.boundingRect().width, NewMtx1.boundingRect().y + NewMtx1.boundingRect().height);
            Core.rectangle(rgba, a2, b2, dark_blue,3);

            double x1, x2, y1, y2;
            x1 = innerCenter.x;
            x2 = outterCenter.x;
            y1 = innerCenter.y;
            y2 = outterCenter.y;

            double k = (y2 - y1) / (x1 - x2);

            if ((k < -1) | (k > 1)) {
                if (y2 >= y1) {
                    extra_angle = 180;
                } else {
                    extra_angle = 0;
                }
            } else if ((-1 < k) | (k < 1)) {
                if (x1 >= x2) {
                    extra_angle = 270;
                } else {
                    extra_angle = 90;
                }
            }

            double finalangle =NewMtx2.angle+90+extra_angle;

            String kk = "k is (" + String.valueOf(k) + ")";
            Core.putText(rgba, kk, new Point(50, 400), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);
            String angle = "rotate angle is (" + String.valueOf(finalangle) + ")";
            Core.putText(rgba, angle, new Point(50, 500), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);

            //Core.putText(rgba, String.valueOf(pContour.size()), new Point(50, 550), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);
            //Core.putText(rgba, String.valueOf(innerCenter.x)+" "+innerCenter.y, new Point(50, 600), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);
            //Core.putText(rgba, String.valueOf(outterCenter.x)+" "+outterCenter.y, new Point(50, 650), Core.FONT_HERSHEY_SIMPLEX, 1, light_blue);

            long stopTime = System.currentTimeMillis();
            String sss = String.valueOf(stopTime - startTime);
            //Log.i(TAG2, "spend_time for one frame = " + sss + " ms");
            //Log.i(TAG,"The coordinator is = " + sss);

            /*if (pContour.size() == 2) {
                Point[] vertices = new Point[4];

                //.points() will place the 4 corner points into vertices.
                NewMtx2.points(vertices);

                String rect = "";
                for (int i = 0; i < vertices.length; i++) {
                    rect += "(" + vertices[i].x + "," + vertices[i].y + ")\n";
                }

                ImageManipulationsActivity.patternCoordinator = new PatternCoordinator(
                        vertices[0],
                        vertices[1],
                        vertices[2],
                        vertices[3],
                        finalangle
                );
            }*/
            PatternCoordinator pc = new PatternCoordinator(a,b,a2,b2,finalangle);
            return pc;
        }
    }

    public List<MatOfPoint> getContoursBySize(int dis, List<MatOfPoint> contour) {
        List<MatOfPoint> con_in_range = new ArrayList<MatOfPoint>();
        //filter out contours out of certain range
        Iterator<MatOfPoint> each = contour.iterator();
        while (each.hasNext()) {
            MatOfPoint contours = each.next();
            if ((Imgproc.contourArea(contours) < dis * 600) & (Imgproc.contourArea(contours) > dis * 20)) {
                con_in_range.add(contours);
            }
        }
        return con_in_range;
    }

    public List<MatOfPoint> getContoursSquare2(List<MatOfPoint> con_in_range) {
        List<MatOfPoint> squareContours = new ArrayList<MatOfPoint>();
        List<Point> cl = new ArrayList<Point>();
        Iterator<Point> con;
        Iterator<MatOfPoint> each_con = con_in_range.iterator();
        Rect out_rect = new Rect();

        while(each_con.hasNext()){
            MatOfPoint contours = each_con.next();
            out_rect = Imgproc.boundingRect(contours);
            if(Math.abs(out_rect.height - out_rect.width) < 10){
                squareContours.add(contours);
            }
        }
        return squareContours;
    }

    public List<MatOfPoint> getContoursSquare(List<MatOfPoint> con_in_range){
        Point point_right = new Point();
        Point point_left = new Point();
        Point point_top = new Point();
        Point point_bottom = new Point();

        double p_x_max = 0;
        double p_x_min = 0;
        double p_y_max = 0;
        double p_y_min = 0;

        List<MatOfPoint> squareContours = new ArrayList<MatOfPoint>();
        List<Point> cl = new ArrayList<Point>();
        Iterator<Point> con;
        Iterator<MatOfPoint> each_con = con_in_range.iterator();

        while(each_con.hasNext()){
            MatOfPoint contours = each_con.next();
            cl = contours.toList();             //transform contour to points of list

            con = cl.iterator();
            int i = 0,j = 0;
            while(con.hasNext()){
                Point p = con.next();
                if(i == 0){
                    point_bottom = p;
                    point_top = p;
                    point_right = p;
                    point_left = p;
                }
                else {
                    if (p.x >= point_right.x) {
                        point_right = p;
                        //p_x_max = p.x;
                        if (p.y <= point_right.y) {
                            point_right = p;
                        }
                    }
                    if (p.x <= point_left.x) {
                        point_left = p;
                        p_x_min = p.x;
                        if (p.y >= point_left.y) {
                            point_left = p;
                        }
                    }
                    if (p.y <= point_top.y) {
                        point_top = p;
                        p_y_max = p.y;
                        if (p.x >= point_top.x) {
                            point_top = p;
                        }
                    }
                    if (p.y >= point_bottom.y) {
                        point_bottom = p;
                        //p_y_min = p.y;
                        if (p.x <= point_bottom.x) {
                            point_bottom = p;
                        }
                    }
                }
                i++;
            }
            //point_top.x = p_y_min;

            //determine the shape of square
            double length1 = Math.sqrt(Math.pow(point_top.x-point_left.x,2)+Math.pow(point_top.y-point_left.y,2));
            double length2 = Math.sqrt(Math.pow(point_left.x-point_bottom.x,2)+Math.pow(point_left.y-point_bottom.y,2));
            double length3 = Math.sqrt(Math.pow(point_bottom.x-point_right.x,2)+Math.pow(point_bottom.y-point_right.y,2));
            double length4 = Math.sqrt(Math.pow(point_top.x-point_right.x,2)+Math.pow(point_top.y-point_right.y,2));
            double areadiff = (length2-length1)+(length4-length3)+(length3-length2)+(length4-length2);

            double slope1 = (point_top.y-point_left.y)/(point_top.x-point_left.x);
            double slope2 = (point_right.y-point_top.y)/(point_right.x-point_top.x);
            double slope3 = (point_bottom.y-point_right.y)/(point_bottom.x-point_right.x);
            double slope4 = (point_left.y-point_bottom.y)/(point_left.x-point_bottom.x);

            double an1 = slope1*slope2*slope3*slope4;

            //The product of slope is around 1, and the sum of length differences of each edge is low.
            if((0.6 <an1)&(an1<1.4)&(Math.abs(areadiff)<50)){
                squareContours.add(contours);
            }
        }
        return squareContours;
    }

    MatOfPoint getContoursOutter(List<MatOfPoint> squareContour ){
        MatOfPoint outterCon = new MatOfPoint();
        MatOfPoint sqr = new MatOfPoint() ;

        Iterator<MatOfPoint> squareCon = squareContour.iterator();
        double maxSquareArea = 0;
        while(squareCon.hasNext()){
            sqr = squareCon.next();
            double SquareArea = Imgproc.contourArea(sqr);
            if(SquareArea > maxSquareArea) {
                outterCon = sqr;
            }
        }
        return outterCon;
    }

    MatOfPoint getContoursInner(List<MatOfPoint> squareContour,MatOfPoint outterContours ){
        MatOfPoint innerCon = new MatOfPoint();
        double areaOut = Imgproc.contourArea(outterContours);
        Iterator<MatOfPoint> squareCon = squareContour.iterator();
        while (squareCon.hasNext()) {
            MatOfPoint sqr = squareCon.next();
            if ((Imgproc.contourArea(sqr) < 0.5*areaOut) & (Imgproc.contourArea(sqr) > 0.1*areaOut)) {
                innerCon = sqr;
            }
        }
        return innerCon;
    }

    Point getShapeCenter(MatOfPoint shapeContour){
        Iterator<Point> con_point = shapeContour.toList().iterator();
        Point shapeCenter = new Point();
        double x_sum = 0;
        double y_sum = 0;
        int count = 0;
        while(con_point.hasNext()){
            Point pt = con_point.next();
            x_sum += pt.x;
            y_sum += pt.y;
            count++;
        }
        shapeCenter.x = x_sum/count;
        shapeCenter.y = y_sum/count;
        return shapeCenter;
    }

    public List<MatOfPoint> findPattern(List<MatOfPoint> contours){
        double distance = 1000000;
        List<MatOfPoint> patternContours = new ArrayList<MatOfPoint>();
        Iterator<MatOfPoint> iter1 = contours.iterator();
        while(iter1.hasNext()){
            MatOfPoint con1 = iter1.next();
            Point center1 = getShapeCenter(con1);
            Iterator<MatOfPoint> iter2 = contours.iterator();
            while(iter2.hasNext()){
                MatOfPoint con2 = iter2.next();
                Point center2 = getShapeCenter(con2);
                double dis_this = Math.abs(center1.x-center2.x)+Math.abs(center1.y-center2.y);
                if((dis_this < distance)&(dis_this>0)){
                    distance = dis_this;
                    patternContours.clear();
                    patternContours.add(con1);
                    patternContours.add(con2);
                }
            }
        }
        return patternContours;
    }
    //</editor-fold>
}