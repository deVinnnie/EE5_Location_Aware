package com.EE5.preferences;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

import com.EE5.R;


public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        /* Set default values for ListViews.*/
        ListPreference connection_type = (ListPreference) findPreference("connection_type");
        if (connection_type.getValue() == null) { // Give a default value
            connection_type.setValue("Bluetooth");
        }
        ListPreference socketTaskType = (ListPreference) findPreference("socketTaskType");
        if (socketTaskType.getValue() == null) {
            socketTaskType.setValue("PRIMITIVE_DATA");
        }
    }
}
