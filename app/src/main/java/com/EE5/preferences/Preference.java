package com.EE5.preferences;

import android.content.Context;
import android.util.AttributeSet;

public class Preference extends android.preference.EditTextPreference{
    public Preference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Preference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Preference(Context context) {
        super(context);
    }

    @Override
    public CharSequence getSummary() {
        //Show the value of the preference on the settings screen instead of the name.
        //This was created for the Device ID setting.
        //This setting is read-only, but should be visible to the user for debugging purposes.
        String summary = super.getSummary().toString();
        return String.format(summary, getText());
    }
}
