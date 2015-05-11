package com.EE5.tests;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.TextView;

import com.EE5.communications.ConnectionActivity;

public class ConnectionActivityTest extends ActivityUnitTestCase<ConnectionActivity> {
    private ConnectionActivity activity;
    private TextView mFirstTestText;

    public ConnectionActivityTest() {
        super(ConnectionActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Starts the MainActivity of the target application
        startActivity(new Intent(getInstrumentation().getTargetContext(), ConnectionActivity.class), null, null);

        // Getting a reference to the MainActivity of the target application
        activity = (ConnectionActivity)getActivity();

        // Getting a reference to the TextView of the MainActivity of the target application
        mFirstTestText = (TextView) activity.findViewById(com.EE5.R.id.txtPosition);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @SmallTest
    public void testHello(){
        // The actual text displayed in the textview
        String actual=mFirstTestText.getText().toString();

        // The expected text to be displayed in the textview
        //String expected = "H";

        // Check whether both are equal, otherwise test fails
        //assertEquals(expected, actual);
    }

    @LargeTest
    public void testButtons(){
        Button view = (Button) activity.findViewById(com.EE5.R.id.buttonConnect);
        assertNotNull("Button not allowed to be null", view);
        /*view.performClick();
        Connection connection = GlobalResources.getInstance().getConnection();
        assertNotNull("Connection should have been created.", connection);*/
    }
}