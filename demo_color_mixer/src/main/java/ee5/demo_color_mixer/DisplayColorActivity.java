package ee5.demo_color_mixer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.EE5.server.data.Position;
import com.EE5.util.GlobalResources;

import java.util.Map;

public class DisplayColorActivity extends ActionBarActivity {
    private Calculator calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_color);

        calc = new Calculator();

        // Get the colors from the intent
        Intent intent = getIntent();
        String myColor = intent.getStringExtra(ColorSelectActivity.EXTRA_MYCOLOR);
        String otherColor = intent.getStringExtra(ColorSelectActivity.EXTRA_OTHERCOLOR);

        // Show selected colors as text.
        TextView myTextView = (TextView) findViewById(R.id.myTextView);
        TextView otherTextView = (TextView) findViewById(R.id.otherTextView);
        myTextView.setText(myColor);
        otherTextView.setText(otherColor);

        // Show my color as background
        BackgroundColor firstColor;
        switch (myColor) {
            case "Red":
                firstColor = BackgroundColor.RED;
                break;
            case "Yellow":
                firstColor = BackgroundColor.YELLOW;
                break;
            case "Blue":
                firstColor = BackgroundColor.BLUE;
                break;
            default:
                throw new IllegalArgumentException("Invalid color: " + myColor);
        }
        RelativeLayout background = (RelativeLayout) findViewById(R.id.myBackground);
        background.setBackgroundColor(Color.argb(firstColor.getA(), firstColor.getR(), firstColor.getG(), firstColor.getB()));
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
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {

            final Handler myHandler = new Handler();

            (new Thread(new Runnable() {

                @Override
                public void run() {
                    TextView myTextView = (TextView) findViewById(R.id.myTextView);
                    TextView otherTextView = (TextView) findViewById(R.id.otherTextView);
                    final String myColor = myTextView.getText().toString();
                    final String otherColor = otherTextView.getText().toString();
                    myHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            changeColor(myColor, otherColor);
                        }
                    });
                }
            })).start();
        }
    }

    public void changeColor(String myColor, String otherColor) {
        RelativeLayout background = (RelativeLayout) findViewById(R.id.myBackground);
        BackgroundColor mixedColor;
        boolean isChanged = false;
        while (!isChanged) {
            if (checkDistance()) {
                isChanged = true;
            }
        }

        switch(myColor) {
            case "Red":
                switch (otherColor) {
                    case "Red":
                        mixedColor = BackgroundColor.WHITE;
                        Context context = getApplicationContext();
                        CharSequence text = "You can not mix two equal colors. Go back to color selection.";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        break;
                    case "Blue":
                        mixedColor = BackgroundColor.PURPLE;
                        break;
                    case "Yellow":
                        mixedColor = BackgroundColor.ORANGE;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid color: " + otherColor);
                }
                break;
            case "Yellow":
                switch (otherColor) {
                    case "Yellow":
                        mixedColor = BackgroundColor.WHITE;
                        Context context = getApplicationContext();
                        CharSequence text = "You can not mix two equal colors. Go back to color selection.";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        break;
                    case "Blue":
                        mixedColor = BackgroundColor.GREEN;
                        break;
                    case "Red":
                        mixedColor = BackgroundColor.ORANGE;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid color: " + otherColor);
                }
                break;
            case "Blue":
                switch (otherColor) {
                    case "Blue":
                        mixedColor = BackgroundColor.WHITE;
                        Context context = getApplicationContext();
                        CharSequence text = "You can not mix two equal colors. Go back to color selection.";
                        int duration = Toast.LENGTH_LONG;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        break;
                    case "Red":
                        mixedColor = BackgroundColor.PURPLE;
                        break;
                    case "Yellow":
                        mixedColor = BackgroundColor.GREEN;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid color: " + otherColor);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid color: " + myColor);
        }
        background.setBackgroundColor(Color.argb(mixedColor.getA(), mixedColor.getR(), mixedColor.getG(), mixedColor.getB()));
    }

    /**
     * Returns true when the distance between two phones is less than 5 cm.
     * @return isClose
     */
    public boolean checkDistance() {
        boolean isClose = false;

        Position ownPosition = GlobalResources.getInstance().getDevice().getPosition();
        calc.setX1(ownPosition.getX());
        calc.setY1(ownPosition.getY());

        //Iterate over other devices.
        for (Map.Entry<String, Position> entry : GlobalResources.getInstance().getDevices().getMap().entrySet()) {
            calc.setX2(entry.getValue().getX());
            calc.setY2(entry.getValue().getY());
            break; //Only read the position of the first device.
        }

        double distance = calc.calcDistance();

        if (distance <= 5) {
            isClose = true;
        }

        return isClose;
    }
}
