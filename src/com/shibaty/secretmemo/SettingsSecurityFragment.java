/**
 * セキュリティ設定Fragment.<br>
 */
package com.shibaty.secretmemo;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * セキュリティ設定Fragmentクラス.<br>
 *
 * @author shibaty
 *
 */
public class SettingsSecurityFragment extends PreferenceFragment {

    /**
     * Fragment生成時の処理.<br>
     *
     * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_security);
    }
}
