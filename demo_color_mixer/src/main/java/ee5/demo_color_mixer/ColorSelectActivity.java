package ee5.demo_color_mixer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.EE5.image_manipulation.PatternDetector;
import com.EE5.util.GlobalResources;

public class ColorSelectActivity extends ActionBarActivity {
    public final static String EXTRA_MYCOLOR = "ee5.colormixer.MYCOLOR";
    public final static String EXTRA_OTHERCOLOR = "ee5.colormixer.OTHERCOLOR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_select);

        //Start new Activity and block until it returns.
        startActivityForResult(new Intent("com.EE5.image_manipulation.ImageManipulationsActivity"), 0);
        //----------------------------------------------

        Spinner mySpinner = (Spinner) findViewById(R.id.my_colors_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.my_colors_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mySpinner.setAdapter(adapter);

        // Initialize other spinner
        Spinner otherSpinner = (Spinner) findViewById(R.id.other_colors_spinner);
        otherSpinner.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_select, menu);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PatternDetector patternDetector = GlobalResources.getInstance().getPatternDetector();
        if(patternDetector != null) {
            patternDetector.destroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PatternDetector patternDetector = GlobalResources.getInstance().getPatternDetector();
        if(patternDetector != null) {
            patternDetector.setup();
        }
    }

    /** Called when the user clicks the Send button */
    public void setColors(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayColorActivity.class);
        Spinner mySpinner = (Spinner) findViewById(R.id.my_colors_spinner);
        Spinner otherSpinner = (Spinner) findViewById(R.id.other_colors_spinner);
        String myColor = (String) mySpinner.getSelectedItem();
        String otherColor = (String) otherSpinner.getSelectedItem();
        intent.putExtra(EXTRA_MYCOLOR, myColor);
        intent.putExtra(EXTRA_OTHERCOLOR, otherColor);
        startActivity(intent);
    }
}
