package com.shibaty.secretmemo;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsSecurityFragment extends PreferenceFragment {

    /**
     * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_security);
    }
}
