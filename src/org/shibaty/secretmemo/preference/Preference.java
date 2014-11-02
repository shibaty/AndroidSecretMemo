/**
 * プリファレンス操作.
 */

package org.shibaty.secretmemo.preference;

import org.shibaty.secretmemo.R;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * プリファレンス操作クラス.<br>
 *
 * @author shibaty
 */
public class Preference {

    /**
     * コンストラクタ.<br>
     */
    private Preference() {
        // nop
    }

    /**
     * SingleTapIsの設定値取得.<br>
     * デフォルト値は動作なし
     *
     * @param context Context
     * @return 設定値
     */
    public static String getSingleTapIs(Context context) {
        SharedPreferences pref = android.preference.PreferenceManager
                .getDefaultSharedPreferences(context);
        String singletap = pref.getString(context.getString(R.string.settings_key_singletap_is),
                context.getString(R.string.settings_list_value_singletap_is_nop));
        return singletap;
    }

    /**
     * SingleTapIsがCopyかを確認する.<br>
     *
     * @param context Context
     * @return true/false
     */
    public static boolean isSingleTapIsCopy(Context context) {
        return getSingleTapIs(context).equals(
                context.getString(R.string.settings_list_value_singletap_is_copy));
    }

    /**
     * SingleTapIsがNotificationかを確認する.<br>
     *
     * @param context Context
     * @return true/false
     */
    public static boolean isSingleTapIsNotification(Context context) {
        return getSingleTapIs(context).equals(
                context.getString(R.string.settings_list_value_singletap_is_notification));
    }
}
