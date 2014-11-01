/**
 * 動作設定Fragment.<br>
 */

package com.shibaty.secretmemo;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

/**
 * 動作設定Fragmentクラス.<br>
 *
 * @author shibaty
 */
public class SettingsMotionFragment extends PreferenceFragment implements
        OnSharedPreferenceChangeListener {

    /**
     * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
     * @param savedInstanceState {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_motion);
        updateSummary();
    }

    /**
     * @see android.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * @see android.app.Fragment#onPause()
     */
    @Override
    public void onPause() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
                this);
        super.onPause();
    }

    /**
     * @see android.content.SharedPreferences.
     *      OnSharedPreferenceChangeListener#onSharedPreferenceChanged(
     *      android.content.SharedPreferences, java.lang.String)
     * @param sharedPreferences {@inheritDoc}
     * @param key {@inheritDoc}
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        updateSummary();
    }

    /**
     * サマリーの表示を更新.
     */
    private void updateSummary() {
        ListPreference listPreference = (ListPreference) findPreference(
                getString(R.string.settings_key_singletap_is));
        listPreference.setSummary(listPreference.getEntry());
    }
}
