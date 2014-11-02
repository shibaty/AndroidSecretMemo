/**
 * ログユーティリティ.
 */

package org.shibaty.secretmemo.util;

import android.util.Log;

/**
 * ログユーティリティクラス.
 *
 * @author shibaty
 */
public class LogUtil {
    /** TAG. */
    private static final String TAG = "secretmemo";

    /**
     * コンストラクタ. インスタンス生成抑止
     */
    private LogUtil() {
        // インスタンス生成抑止
    }

    /**
     * メソッドの開始を記録.
     */
    public static void methodStart() {
        Log.i(TAG, "Method Start: "
                + Thread.currentThread().getStackTrace()[3].getClassName() + "#"
                + Thread.currentThread().getStackTrace()[3].getMethodName());
    }

    /**
     * メソッドの終了を記録.
     */
    public static void methodEnd() {
        Log.i(TAG, "Method End: "
                + Thread.currentThread().getStackTrace()[3].getClassName() + "#"
                + Thread.currentThread().getStackTrace()[3].getMethodName());
    }

    /**
     * デバッグログを記録.
     *
     * @param message メッセージ
     */
    public static void logDebug(String message) {
        Log.d(TAG, "Funciton: "
                + Thread.currentThread().getStackTrace()[3].getClassName() + "#"
                + Thread.currentThread().getStackTrace()[3].getMethodName()
                + "[" + message + "]");
    }

    /**
     * ログレベルがDEBUG以下かを判定.
     *
     * @return DEBUGの場合、true
     */
    public static boolean isDebugable() {
        return Log.isLoggable(TAG, Log.DEBUG);
    }

}
