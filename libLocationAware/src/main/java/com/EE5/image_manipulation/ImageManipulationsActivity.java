package com.EE5.image_manipulation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.EE5.R;
import com.EE5.client.IDGenerator;
import com.EE5.communications.ConnectionActivity;
import com.EE5.communications.ServerActivity;
import com.EE5.math.Calc;
import com.EE5.preferences.SettingsActivity;
import com.EE5.server.Server;
import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;
import com.EE5.util.Tuple;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;

import java.text.DecimalFormat;
import java.util.Map;


public class ImageManipulationsActivity extends Activity {


    public boolean OpenCVReady;
    /**
     * The code in onManagerConnected is called after the OpenCV stuff has loaded.
     * If you try this for instance in the onCreate method it will fail and you won't have a clue as to why it did so.
     * This is because OpenCV components (even simple things like the Mat class) are used
     * before the library is loaded it will throw an exception.
     */
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    onOpenCVSuccessLoad();
                    synchronized (ImageManipulationsActivity.this){
                        ImageManipulationsActivity.this.notify();
                    }
                }
                break;
                default: {
                    Log.i("OpenCV", "OpenCV did not load correctly");
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    private void onOpenCVSuccessLoad(){
        Log.i("OpenCV", "OpenCV loaded successfully");
        if(GlobalResources.getInstance().getPatternDetector() == null) {
            //Only Execute this code if a PatternDetector already exists from a previous run.
            setupPatternDetector();
            setupCamera(); //Retrieve camera WITHOUT CameraBridgeViewBase and JavaImageView
        }
        this.OpenCVReady = true;
    }

    private TextView tx_x1;
    private TextView tx_x2;
    private TextView tx_y1;
    private TextView tx_y2;
    private TextView rotate;
    private ImageView iv;
    private MenuItem mItemPreviewRGBA;
    private MenuItem mItemPreviewCanny;

    private PatternDetector patternDetector;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Read out preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //Set device ID:
        String id = IDGenerator.generate(sharedPref);
        GlobalResources.getInstance().getDevice().setId(id);

        //Obviously this line will prevent the screen from turning off.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.image_manipulations_surface_view);

        tx_x1 = (TextView) findViewById(R.id.x_axis);
        tx_x2 = (TextView) findViewById(R.id.x_axis2);
        tx_y1 = (TextView) findViewById(R.id.y_axis);
        tx_y2 = (TextView) findViewById(R.id.y_axis2);
        rotate = (TextView) findViewById(R.id.angle);

        iv = (ImageView) findViewById(R.id.imageView);
        //iv.setVisibility(View.GONE);

        Button btn_get = (Button) findViewById(R.id.btn_get);
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //rotate.setText(String.valueOf(patternDetector.getPatternDetectorAlgorithm().getDistance()));
                //tx_y1.setText(String.valueOf(patternDetector.getPatternDetectorAlgorithm().ra));
                patternDetector.getPatternDetectorAlgorithm().setSetupflag(false);
                patternDetector.getPatternDetectorAlgorithm().setDistance2(0);
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
                int distance = seekBar.getProgress();
                patternDetector.getPatternDetectorAlgorithm().setDistance(distance);
                tx_y2.setText(String.valueOf(distance));

                //Due to complications (See PatternDetectorInterface) some variables cannot be directly accessed anymore.
            }
        });
    }

    /**
     * Makes a new PatternDetector instance and saves it to GlobalResources.
     * Doesn't start the camera.
     */
    private void setupPatternDetector(){
        try {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            double pattern_width = Double.parseDouble(sharedPref.getString("pattern_size", "11.75")); //Second param is default value.

            //Select front or back facing camera.
            boolean forceBackFacingCamera = sharedPref.getBoolean("camera", false);
            int cameraSelection = (forceBackFacingCamera) ? 0 : 1;

            if(Camera.getNumberOfCameras()<2){
                cameraSelection = 0;
                //Use rear facing camera if there is no front facing camera.
            }

            //Derive the image size.
            Camera camera = Camera.open(cameraSelection); //Be careful : this locks the camera!
            Camera.Parameters params = camera.getParameters();
            Camera.Size imageSize = params.getPictureSize();
            camera.release();
            Log.i("ImageSize", imageSize.width + " " + imageSize.height);
            Calc calc = new Calc(pattern_width, imageSize.width, imageSize.height);

            //Make new PatternDetector.
            this.patternDetector = new PatternDetector(cameraSelection);
            this.patternDetector.setHandler(this.handler);
            this.patternDetector.setCalc(calc);
            GlobalResources.getInstance().setPatternDetector(this.patternDetector);
        }
        catch(RuntimeException ex){
            CharSequence text = "Camera not available";
            Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupCamera(){
        if(this.patternDetector != null) {
            this.patternDetector.setup();
            this.patternDetector.setViewMode(PatternDetector.VIEW_MODE_CANNY);
        }
    }

    // For an overview of when and which events are called see:
    // http://www.tutorialspoint.com/android/android_acitivities.htm
    //<editor-fold desc="Android Activity Lifecycle Events">
    @Override
    public void onPause() {
        super.onPause();
        PatternDetector patternDetector = GlobalResources.getInstance().getPatternDetector();
        if(patternDetector != null) {
            patternDetector.destroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PatternDetector patternDetector = GlobalResources.getInstance().getPatternDetector();
        if(patternDetector != null) {
            //patternDetector.getPatternDetectorAlgorithm().distance2 = 0;
            patternDetector.setup();
        }
        Log.i("OpenCV", "OpenCV loading");
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
        synchronized (this) {
            try {
                this.wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void onDestroy() {
        super.onDestroy();
        //'patternDetector.destroy()' is not called here because onDestroy() is called after onPause().
        //The destroy method is already called in onPause()
    }
    //</editor-fold>

    //<editor-fold desc="Options">
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.global, menu);

        //mItemPreviewRGBA  = menu.add("Preview RGBA");
        //mItemPreviewCanny = menu.add("Canny");

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

    //<editor-fold desc="View updates">
    /**
     * A reference to this handler is passed to the {@link PatternDetector} instance.
     * This enables the {@link PatternDetector} to send the signal to update the UI.
     * (UI changes can only be run on the main thread. The {@link PatternDetector} code runs on a separate thread.)
     */
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                ImageManipulationsActivity.this.updateView();
                ImageManipulationsActivity.this.updateServerPositions();
            }
            super.handleMessage(msg);
        }
    };

    private void updateView() {
        // Convert the matrix to an Android bitmap
        // As a means of testing the bitmap is displayed in an ImageView.
        // (iv was already available...)
        // In the final version this would be disabled
        // (at least for the most part of the run of the application)
        Bitmap bitmap = Bitmap.createBitmap(this.patternDetector.image.cols(), this.patternDetector.image.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(this.patternDetector.image, bitmap, true);
        iv.setImageBitmap(bitmap);
        tx_x2.setText(String.valueOf(patternDetector.getPatternDetectorAlgorithm().getDistance2()));
    }

    /**
     * Refreshes the overlay text using the latest positions.
     */
    private void updateServerPositions(){
        Server server = GlobalResources.getInstance().getServer();
        Position devicePosition = GlobalResources.getInstance().getDevice().getPosition();

        //Only execute this code when running as server.
        if(server != null) {
            String list=""; //Temporary variable for building the text for the TextView overlay.

            //Iterate over all devices and save relevant information to TextView.
            for (Map.Entry<String, Tuple<Position, String>> entry : server.getConnectedDevices().getAll()) {
                String id = entry.getKey();
                Position position = entry.getValue().element1;
                list+=""+id +"\n";
                DecimalFormat df = new DecimalFormat("#.##"); //Round to two decimal places.

                list += "x:"+df.format(position.getX())+"; y:" + df.format(position.getY()) +"; z:" + df.format(position.getHeight()) + "; rot:" + df.format(position.getRotation());
                list+="\n";

                double distance = Calc.getDistance(position, devicePosition);
                list+="distance="+distance+"\n";
                list+="data=" + entry.getValue().element2+"\n";
            }

            TextView server_display = (TextView) findViewById(R.id.server_display);
            server_display.setText(list);
        }
    }
    //</editor-fold>
}