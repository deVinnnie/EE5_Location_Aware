package ee5.demo_color_mixer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.EE5.image_manipulation.PatternDetector;
import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;
import com.EE5.util.Tuple;

import java.util.Map;

public class DisplayColorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_color);
        setupColors();
    }



    /** Called when the activity is about to become visible. */
    @Override
    protected void onStart() {
        super.onStart();
        setupColors();
    }

    /** Called when the activity has become visible. */
    @Override
    protected void onResume() {
        super.onResume();
        PatternDetector patternDetector = GlobalResources.getInstance().getPatternDetector();
        if(patternDetector != null) {
            patternDetector.setup();
        }
    }

    /** Called when another activity is taking focus. */
    @Override
    protected void onPause() {
        super.onPause();
        PatternDetector patternDetector = GlobalResources.getInstance().getPatternDetector();
        if(patternDetector != null) {
            patternDetector.destroy();
        }
    }

    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /** Activity being restarted from stopped state. */
    @Override
    protected void onRestart() {
        super.onRestart();
        setupColors();
    }

    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        loopHandler.removeCallbacks(loopRunnable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_color, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void changeColor(String myColor, String otherColor) {
        RelativeLayout background = (RelativeLayout) findViewById(R.id.myBackground);
        BackgroundColor mixedColor = BackgroundColor.WHITE;
        boolean inRange = checkDistance();
        if (!inRange) {
            // Do nothing
        }
        else  {
            mixedColor.setR(calculateR(myColor, otherColor));
            mixedColor.setG(calculateG(myColor, otherColor));
            mixedColor.setB(calculateB(myColor, otherColor));
            background.setBackgroundColor(Color.argb(mixedColor.getA(), mixedColor.getR(), mixedColor.getG(), mixedColor.getB()));
        }
    }

    private int calculateR(String myColor, String otherColor) {
        int rValue;
        BackgroundColor mColor = checkColor(myColor);
        BackgroundColor oColor = checkColor(otherColor);
        if (oColor.getR() == 255) {
            rValue = Math.min(((mColor.getR()) + (oColor.getR() - (2 * calc.calcDistance()))), 255);
        }
        else {
            rValue = Math.min(((mColor.getR()) + (oColor.getR())), 255);
        }
        return rValue;
    }

    private int calculateG(String myColor, String otherColor) {
        int gValue;
        BackgroundColor mColor = checkColor(myColor);
        BackgroundColor oColor = checkColor(otherColor);
        if (oColor.getG() == 255) {
            gValue = Math.min(((mColor.getG()) + (oColor.getG() - (2 * calc.calcDistance()))), 255);
        }
        else {
            gValue = Math.min(((mColor.getG()) + (oColor.getG())), 255);
        }
        return gValue;
    }

    private int calculateB(String myColor, String otherColor) {
        int bValue;
        BackgroundColor mColor = checkColor(myColor);
        BackgroundColor oColor = checkColor(otherColor);
        if (oColor.getB() == 255) {
            bValue = Math.min(((mColor.getB()) + (oColor.getB() - (2 * calc.calcDistance()))), 255);
        }
        else {
            bValue = Math.min(((mColor.getB()) + (oColor.getB())), 255);
        }
        return bValue;
    }

    private BackgroundColor checkColor(String color) {
        BackgroundColor bColor;
        switch (color) {
            case "Red":
                bColor = BackgroundColor.RED;
                break;
            case "Green":
                bColor = BackgroundColor.GREEN;
                break;
            case "Blue":
                bColor = BackgroundColor.BLUE;
                break;
            default:
                throw new IllegalArgumentException("Invalid color: " + color);
        }
        return bColor;
    }

    /**
     * Returns true when the distance between two phones is between 1 and 100 cm.
     * @return inRange
     */
    private boolean checkDistance() {
        boolean inRange = false;

        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
        calc.setX1(ownPosition.getX());
        calc.setY1(ownPosition.getY());

        //Iterate over other devices.
        for (Map.Entry<String, Tuple<Position,String>> entry : GlobalResources.getInstance().getDevices().getAll()) {
            calc.setX2(entry.getValue().element1.getX());
            calc.setY2(entry.getValue().element1.getY());
            Log.d("checkserver", "check other phone x: " + calc.getX2() + " y: " + calc.getY2());
            Log.i("T", entry.getValue().element2);
            break; //Only read the position of the first device.
        }

        int distance = calc.calcDistance();

        if (distance >= 1 && distance <= 100) {
            inRange = true;
        }

        return inRange;
    }

    private void setupColors() {
        calc = new Calculator();

        // Get the colors from the intent
        Intent intent = getIntent();
        myColor = intent.getStringExtra(ColorSelectActivity.EXTRA_MYCOLOR);
        otherColor = intent.getStringExtra(ColorSelectActivity.EXTRA_OTHERCOLOR);

        // Show selected colors as text.
        TextView myTextView = (TextView) findViewById(R.id.myTextView);
        TextView otherTextView = (TextView) findViewById(R.id.otherTextView);
        myTextView.setText(myColor);
        otherTextView.setText(otherColor);

        // Show my color as background
        BackgroundColor firstColor = checkColor(myColor);
        RelativeLayout background = (RelativeLayout) findViewById(R.id.myBackground);
        background.setBackgroundColor(Color.argb(firstColor.getA(), firstColor.getR(), firstColor.getG(), firstColor.getB()));

        // Start thread to change colors
        loopHandler.post(loopRunnable);
    }

    private String myColor;
    private String otherColor;
    private Calculator calc;
    private Handler loopHandler = new Handler();
    private Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {

            changeColor(myColor, otherColor);

            //Execute this code again after <sample_rate> milliseconds.
            loopHandler.postDelayed(loopRunnable, 100);
        }
    };
}
