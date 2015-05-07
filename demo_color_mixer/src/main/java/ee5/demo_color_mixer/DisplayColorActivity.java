package ee5.demo_color_mixer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DisplayColorActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_color);

        // Get the colors from the intent
        Intent intent = getIntent();
        String myColor = intent.getStringExtra(ColorSelectActivity.EXTRA_MYCOLOR);
        String otherColor = intent.getStringExtra(ColorSelectActivity.EXTRA_OTHERCOLOR);

        // Show selected colors as text. Later: set background color
        TextView myTextView = (TextView) findViewById(R.id.myTextView);
        TextView otherTextView = (TextView) findViewById(R.id.otherTextView);
        myTextView.setText(myColor);
        otherTextView.setText(otherColor);

        // Show my color as background
        RelativeLayout background = (RelativeLayout) findViewById(R.id.myBackground);
        BackgroundColor firstColor = BackgroundColor.RED;
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
}
