package com.EE5.image_manipulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.EE5.communications_test.PositionActivity;
import com.EE5.communications_test.ServerActivity;
import com.EE5.preferences.SettingsActivity;

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
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImageManipulationsActivity extends Activity implements CvCameraViewListener2 {
    private static final String  TAG                 = "OCVSample::Activity";
    private static final String  TAG2                 = "Running Time";

    public static final int      VIEW_MODE_RGBA      = 0;
    public static final int      VIEW_MODE_CANNY     = 2;

    private MenuItem             mItemPreviewRGBA;
    private MenuItem             mItemPreviewCanny;
    private CameraBridgeViewBase mOpenCvCameraView;

    private Size                 mSize0;

    private Mat                  mIntermediateMat;
    private Mat                  mSepiaKernel;

    public static int           viewMode = VIEW_MODE_RGBA;

    TextView tx_x1;
    TextView tx_x2;
    TextView tx_y1;
    TextView tx_y2;
    TextView rotate;

    int distance;

    double p_in_left = 0;
    double p_in_right = 0;
    double p_in_bottom = 0;
    double p_in_top = 0;
    double p_left = 0;
    double p_right = 0;
    double p_bottom = 0;
    double p_top = 0;
    double p_left2 = 0;
    double p_right2 = 0;
    double p_bottom2 = 0;
    double p_top2 = 0;

    ImageView iv;

    public static PatternCoordinator patternCoordinator = new PatternCoordinator(
            new Point(1,1),
            new Point(1,1),
            new Point(1,1),
            new Point(1,1)
    );
    //Change later to non-static variable.


    private BaseLoaderCallback  mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public ImageManipulationsActivity() {
        //Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.image_manipulations_surface_view);

        tx_x1 = (TextView)findViewById(R.id.x_axis);
        tx_x2 = (TextView)findViewById(R.id.x_axis2);
        tx_y1 = (TextView)findViewById(R.id.y_axis);
        tx_y2 = (TextView)findViewById(R.id.y_axis2);
        rotate = (TextView)findViewById(R.id.angle);

        iv = (ImageView)findViewById(R.id.imageView);

        Button btn_get = (Button) findViewById(R.id.btn_get);
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x1 = String.valueOf(p_right);
                String x2 = String.valueOf(p_left);
                String y1 = String.valueOf(p_top);
                String y2 = String.valueOf(p_bottom);

                tx_x1.setText(x1);
                tx_x2.setText(x2);
                tx_y1.setText(y1);
                tx_y2.setText(y2);

                //iv.setImageMatrix();

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
        //mOpenCvCameraView.setCameraIndex(1);
        mOpenCvCameraView.setCameraIndex(0);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

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

        //Links to the Preferences screen.
        Intent prefsIntent = new Intent(this, SettingsActivity.class);
        MenuItem preferences = menu.findItem(R.id.action_settings);
        preferences.setIntent(prefsIntent);

        //Option to start as server.
        Intent serverIntent = new Intent(this, ServerActivity.class);
        MenuItem useAsServer = menu.findItem(R.id.action_use_as_server);
        useAsServer.setIntent(serverIntent);

        //Option to start as client.
        Intent clientIntent = new Intent(this, PositionActivity.class);
        MenuItem useAsClient = menu.findItem(R.id.action_use_as_client);
        useAsClient.setIntent(clientIntent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item == mItemPreviewRGBA) {
            viewMode = VIEW_MODE_RGBA;
            return true;
        }
        else if (item == mItemPreviewCanny) {
            viewMode = VIEW_MODE_CANNY;
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(item.getIntent());
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
        //return true;
    }

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
        Mat rgba = inputFrame.rgba(); //transform frames from camera to rgba color type
        Mat rgba2 = inputFrame.rgba();
        Mat gray2 = inputFrame.gray();

        Size sizeRgba = rgba.size();
        PatternCoordinator pattern;

            switch (ImageManipulationsActivity.viewMode) {
                case ImageManipulationsActivity.VIEW_MODE_RGBA:
                    break;

                case ImageManipulationsActivity.VIEW_MODE_CANNY:
                    patternCoordinator = patternDetection(rgba2,gray2);
                    if(patternCoordinator == null){
                        patternCoordinator = new PatternCoordinator(
                                new Point(1,1),
                                new Point(1,1),
                                new Point(1,1),
                                new Point(1,1)
                        );
                    }
                   // patternEvaluation(pattern,rgba2);
                    break;
            }
        //Imgproc.resize(rgba,rgba,new Size(352,288));
        return rgba;
    }

    public PatternCoordinator patternDetection(Mat rgba,Mat gray2){
        Point point_in_right = new Point();
        Point point_in_left = new Point();
        Point point_in_top = new Point();
        Point point_in_bottom = new Point();
        Point point_right = new Point();
        Point point_left = new Point();
        Point point_top = new Point();
        Point point_bottom = new Point();
        Point point_right2 = new Point();
        Point point_left2 = new Point();
        Point point_top2 = new Point();
        Point point_bottom2 = new Point();

        double maxarea = 0;
        double area = 0;
        double areadiff = 0;

        double extra_angle = 0;

        Point innerCenter = new Point();
        Point outterCenter = new Point();

        List<MatOfPoint> contour = new ArrayList<MatOfPoint>();
        List<Point> cl = new ArrayList<Point>();
        List<Point> cl2 = new ArrayList<Point>();
        Mat hierarchys = new Mat();

        long startTime=System.currentTimeMillis();
        //Imgproc.resize(gray2,gray2,process_size);

        Imgproc.threshold(gray2, mIntermediateMat, 80, 255,Imgproc.THRESH_BINARY);
        //Imgproc.Canny(rgba, mIntermediateMat, 80, 90);                           //mIntermediateMat is a Mat format variable in field

        Imgproc.findContours(mIntermediateMat,contour,hierarchys,Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
        Scalar s = new Scalar(255,120,0);

        List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();
        Iterator<MatOfPoint> each = contour.iterator();
        while (each.hasNext()) {
            MatOfPoint contours = each.next();
            if ((Imgproc.contourArea(contours) < distance*200) & (Imgproc.contourArea(contours) > distance*20)) {
                //Core.multiply(contours, new Scalar(4, 4), contours);
                mContours.add(contours);
            }
        }

        List<MatOfPoint> squareContours = new ArrayList<MatOfPoint>();
        List<MatOfPoint> outterContours = new ArrayList<MatOfPoint>();
        Iterator<Point> con = cl.iterator();
        each = mContours.iterator();
        cl.clear();
        while(each.hasNext()){
            MatOfPoint contours = each.next();
            cl = contours.toList();             //transform contour to points of list

            con = cl.iterator();
            int i=0,j = 0;
            while(con.hasNext()){
                Point p = con.next();

                if(i == 0){
                    p_right = p.x;
                    p_left = p.x;
                    p_top = p.y+100;
                    p_bottom = p.y;
                }

                if(p.x > p_right){p_right = p.x;     point_right = p;}
                if(p.x < p_left){ p_left = p.x;       point_left = p;}
                if(p.y > p_bottom){ p_bottom = p.y;   point_bottom = p;}
                if(p.y < p_top){ p_top = p.y;         point_top = p;}
                i++;
            }

            //determine the shape of square
            double length1 = Math.sqrt(Math.pow(point_top.x-point_left.x,2)+Math.pow(point_top.y-point_left.y,2));
            double length2 = Math.sqrt(Math.pow(point_left.x-point_bottom.x,2)+Math.pow(point_left.y-point_bottom.y,2));
            double length3 = Math.sqrt(Math.pow(point_bottom.x-point_right.x,2)+Math.pow(point_bottom.y-point_right.y,2));
            double length4 = Math.sqrt(Math.pow(point_top.x-point_right.x,2)+Math.pow(point_top.y-point_right.y,2));
            areadiff = (length2-length1)+(length4-length3)+(length3-length2)+(length4-length2);


            double slope1 = (point_top.y-point_left.y)/(point_top.x-point_left.x);
            double slope2 = (point_right.y-point_top.y)/(point_right.x-point_top.x);
            double slope3 = (point_bottom.y-point_right.y)/(point_bottom.x-point_right.x);
            double slope4 = (point_left.y-point_bottom.y)/(point_left.x-point_bottom.x);

            double an1 = slope1*slope2*slope3*slope4;

            //The product of slope is around 1, and the sum of length differences of each edge is low.
            if((0.8<an1)&(an1<1.2)&(Math.abs(areadiff)<30)){
                area = Imgproc.contourArea(contours);
                if(j==0){
                    maxarea = area;
                    outterContours.add(contours);
                }
                if(area>=maxarea){
                    maxarea = area;
                    outterContours.clear();
                    outterContours.add(contours);
                }
                squareContours.add(contours);
                j++;
            }
        }

        List<MatOfPoint> innerContours = new ArrayList<MatOfPoint>();
        MatOfPoint patternOut;
        double areaOut = 0;
        if(outterContours.size() == 1){
            patternOut = outterContours.get(0);
            areaOut = Imgproc.contourArea(patternOut);
            Iterator<MatOfPoint> squareCon = squareContours.iterator();
            while (squareCon.hasNext()) {
                MatOfPoint sqr = squareCon.next();
                if ((Imgproc.contourArea(sqr) < 0.5*areaOut) & (Imgproc.contourArea(sqr) >0.1*areaOut)) {
                    innerContours.add(sqr);
                }
            }
        }

    if(innerContours.size()==1) {
        List<Point> cl3 = new ArrayList<Point>();
        Iterator<Point> inCon;
        cl3 = innerContours.get(0).toList();             //transform contour to points of list
        inCon = cl3.iterator();
        int ii = 0, j = 0;
        while (inCon.hasNext()) {
            Point p = inCon.next();

            if (ii == 0) {
                p_in_right = p.x;
                p_in_left = p.x;
                p_in_top = p.y + 100;
                p_in_bottom = p.y;
            }

            if (p.x > p_in_right) {
                p_in_right = p.x;
                point_in_right = p;
            }
            if (p.x < p_in_left) {
                p_in_left = p.x;
                point_in_left = p;
            }
            if (p.y > p_in_bottom) {
                p_in_bottom = p.y;
                point_in_bottom = p;
            }
            if (p.y < p_in_top) {
                p_in_top = p.y;
                point_in_top = p;
            }
            ii++;
        }

        innerCenter.x = (point_in_left.x+point_in_right.x)/2;
        innerCenter.y = (point_in_top.y+point_in_bottom.y)/2;
    }

        Iterator<MatOfPoint> each2 = squareContours.iterator();
        cl2.clear();
        while(each2.hasNext()){
            MatOfPoint contours = each2.next();
            cl2 = contours.toList();

            con = cl2.iterator();
            int i = 0;
            while(con.hasNext()){
                Point p = con.next();
                if(i == 0){
                    p_right2 = p.x;
                    p_left2 = p.x;
                    p_top2 = p.y+100;
                    p_bottom2 = p.y;
                    point_right2 = p;
                    point_left2 = p;
                    point_bottom2 = p;
                    point_top2 = p;
                }

                if(p.x > point_right2.x){p_right2 = p.x;     point_right2 = p;}
                if(p.x < point_left2.x){ p_left2 = p.x;       point_left2 = p;}
                if(p.y > point_bottom2.y){ p_bottom2 = p.y;   point_bottom2 = p;}
                if(p.y < point_top2.y){ p_top2 = p.y;         point_top2 = p;}
                i++;
            }
            outterCenter.x = (point_left2.x + point_right2.x)/2;
            outterCenter.y = (point_top2.y + point_bottom2.y)/2;
        }

        //void findContours(Mat image, List<MatOfPoint> contours, Mat hierarchy, int mode, int method)
        Imgproc.cvtColor(mIntermediateMat, rgba, Imgproc.COLOR_GRAY2BGRA, 4);

        Imgproc.drawContours(rgba,mContours,-1,new Scalar(0,255,255),4);
        Imgproc.drawContours(rgba,outterContours,-1,s,4);
        Imgproc.drawContours(rgba,innerContours,-1,new Scalar(120,120,255),4);

        if((innerCenter.x<outterCenter.x)&(innerCenter.y<outterCenter.y)){
            extra_angle = 0;
        }
        else if((innerCenter.x>outterCenter.x)&(innerCenter.y<outterCenter.y)){
            extra_angle = 90;
        }
        else if((innerCenter.x>outterCenter.x)&(innerCenter.y>outterCenter.y)){
            extra_angle = 180;
        }
        else if((innerCenter.x<outterCenter.x)&(innerCenter.y>outterCenter.y)){
            extra_angle = 270;
        }

        double resule_rot;

        double xie = Math.sqrt( Math.pow((point_top2.y-point_bottom2.y),2)+ Math.pow((point_top2.x-point_bottom2.x),2));
        double dui = Math.abs(point_top2.y-point_bottom2.y);
        double ling = point_top2.x-point_bottom2.x;
        //resule_rot = Math.toDegrees(Math.asin(dui/xie));
        resule_rot = Math.toDegrees(Math.acos(ling/xie))-90;//+extra_angle;

        Scalar co = new Scalar(255,0,255);
        Scalar co2 = new Scalar(0,255,0);

        String le = "left ("+point_left2.x+","+point_left2.y+")";
        String ri = "right ("+point_right2.x+","+point_right2.y+")";
        String to = "top ("+point_top2.x+","+point_top2.y+")";
        String bo = "bottom ("+point_bottom2.x+","+point_bottom2.y+")";
        String angle = "angle is "+resule_rot;

        Core.line(rgba,point_left2,point_top2,new Scalar(255,0,120),3);
        Core.line(rgba,point_top2,point_right2,new Scalar(255,0,120),3);
        Core.line(rgba,point_bottom2,point_right2,new Scalar(255,0,120),3);
        Core.line(rgba,point_left2,point_bottom2,new Scalar(255,0,120),3);
        Core.line(rgba,innerCenter,outterCenter,new Scalar(120,120,120),3);
        Core.line(rgba,outterCenter,new Point(600,outterCenter.y),new Scalar(120,120,120),3);
        //Core.putText(rgba,String.valueOf(areadiff),point_top2,   Core.FONT_HERSHEY_SIMPLEX,0.5,new Scalar(255,0,255));
        Core.putText(rgba,angle,new Point(18,700), Core.FONT_HERSHEY_SIMPLEX,5,co);
        Core.putText(rgba,le,point_left2,   Core.FONT_HERSHEY_SIMPLEX,0.5,co);
        Core.putText(rgba,ri,point_right2,  Core.FONT_HERSHEY_SIMPLEX,0.5,co);
        Core.putText(rgba,to,point_top2,    Core.FONT_HERSHEY_SIMPLEX,0.5,co);
        Core.putText(rgba,bo,point_bottom2, Core.FONT_HERSHEY_SIMPLEX,0.5,co);
        Core.circle(rgba,point_left2, 3, co2, 1);
        Core.circle(rgba,point_right2,3, co2,1);
        Core.circle(rgba,point_top2,3,co2,1);
        Core.circle(rgba,point_bottom2,3,co2,1);

        PatternCoordinator pc = new PatternCoordinator(point_left2,point_top2,point_right2,point_bottom2);
        int y1 = (int)point_top2.y;
        int y2 = (int)point_bottom2.y;
        int x1 = (int)point_left2.x;
        int x2 = (int)point_right2.x;
        Mat pattern =new Mat();// = rgba.submat(x1,x2,y1,y2);

        long stopTime=System.currentTimeMillis();
        String sss = String.valueOf(stopTime - startTime);
        Log.i(TAG2,"spend_time for one frame = " + sss + " ms");
        //Log.i(TAG,"The coordinator is = " + sss);
        return pc;
    }
}
