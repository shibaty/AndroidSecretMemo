package com.shibaty.secretmemo;

import java.util.List;

import android.preference.PreferenceActivity;

/**
 * @author shibaty
 *
 */
public class SettingsActivity extends PreferenceActivity {

    /* (非 Javadoc)
     * @see android.preference.PreferenceActivity#onBuildHeaders(java.util.List)
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.settings_headers, target);
    }

}
