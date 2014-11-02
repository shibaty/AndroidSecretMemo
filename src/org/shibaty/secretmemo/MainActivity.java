/**
 * メインActivity.<br>
 *
 * @author shibaty
 */

package org.shibaty.secretmemo;

import java.util.List;

import org.shibaty.secretmemo.db.MemoDao;
import org.shibaty.secretmemo.db.MemoEntity;
import org.shibaty.secretmemo.preference.Preference;
import org.shibaty.secretmemo.util.LogUtil;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * メインActivityクラス.<br>
 *
 * @author shibaty
 */
public class MainActivity extends Activity {

    /** Shimeji - Mushroom Intent Action.<br> */
    private static final String MUSHROOM_ACTION =
            "com.adamrocker.android.simeji.ACTION_INTERCEPT";

    /** Shimeji - Mushroom Output Extra Key.<br> */
    private static final String MUSHROOM_OUTPUT_EXTRA_KEY = "replace_key";

    /** ClipData用ラベル.<br> */
    private static final String CLIPDATA_LABEL = "memo_text";

    /** DAO.<br> */
    private MemoDao dao;

    /** マッシュルームフラグ.<br> */
    private boolean isMushroom;

    /**
     * Activity生成時の処理.<br>
     *
     * @see android.content.Context#onCreate(Bundle)
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (LogUtil.isDebugable()) {
            LogUtil.methodStart();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 最初のリストフラグメントを追加
        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            MemoListFragment fragment = new MemoListFragment();
            ft.add(R.id.fragment_container, fragment);
            ft.commit();
        }

        // Intentからマッシュルームかどうかを判断
        Intent intent = getIntent();
        String action = intent.getAction();
        isMushroom = false;
        if (MUSHROOM_ACTION.equals(action)) {
            isMushroom = true;
        }

        if (LogUtil.isDebugable()) {
            LogUtil.methodEnd();
        }
    }

    /**
     * Activity再開時の処理.<br>
     *
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        // データアクセス開始
        dao = new MemoDao(this);
        dao.init();
    }

    /**
     * Activity中断時の処理.<br>
     *
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {

        // データアクセス終了
        dao.finish();

        super.onPause();
    }

    /**
     * OptionsMenu生成.<br>
     *
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     * @param menu Menu
     * @return 処理結果
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * OptionsMenuのアイテム選択時の処理.<br>
     *
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     * @param item MenuItem
     * @return 処理結果
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;

        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(
                        this.getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                result = true;
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

    /**
     * アイテム編集Fragmentに遷移.<br>
     *
     * @param entity 編集対象のメモ
     */
    public void moveToItemEditFragment(MemoEntity entity) {
        if (LogUtil.isDebugable()) {
            LogUtil.methodStart();
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ItemEditFragment fragment = new ItemEditFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ItemEditFragment.ARGKEY_ENTITY, entity);
        fragment.setArguments(bundle);
        ft.replace(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();

        if (LogUtil.isDebugable()) {
            LogUtil.methodEnd();
        }
    }

    /**
     * DAOからメモリストを取得.<br>
     *
     * @return メモリスト
     */
    public List<MemoEntity> getAllItem() {
        return dao.getAll();
    }

    /**
     * メモをDAOに追加.<br>
     *
     * @param entity メモ
     */
    public void addItem(MemoEntity entity) {
        dao.insert(entity);
    }

    /**
     * DAOのメモを更新.<br>
     *
     * @param entity メモ
     */
    public void updateItem(MemoEntity entity) {
        dao.update(entity);
    }

    /**
     * DAOからメモを削除.<br>
     *
     * @param entity メモ
     */
    public void deleteItem(MemoEntity entity) {
        dao.delete(entity);
    }

    /**
     * メモがタップされたときの処理を行う.<br>
     * マッシュルームの場合はマッシュルーム動作、それ以外はPreferenceに従う.
     *
     * @param entity メモ
     */
    public void tapItem(MemoEntity entity) {
        if (isMushroom) {
            Intent intent = new Intent();
            intent.putExtra(MUSHROOM_OUTPUT_EXTRA_KEY, entity.getMemo());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (Preference.isSingleTapIsCopy(getApplicationContext())) {
                copyItem(entity);
            } else if (Preference.isSingleTapIsNotification(
                    getApplicationContext())) {
                notificationItem(entity);
            }
        }
    }

    /**
     * メモをクリップボードにコピー.<br>
     *
     * @param entity メモ
     */
    public void copyItem(MemoEntity entity) {
        ClipData clip = ClipData.newPlainText(CLIPDATA_LABEL, entity.getMemo());
        ClipboardManager cm =
                (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cm.setPrimaryClip(clip);
        Toast.makeText(
                this, R.string.toast_copy_to_clipboard,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * メモを通知バーに表示.<br>
     *
     * @param entity メモ
     */
    public void notificationItem(MemoEntity entity) {
        Notification.Builder builder =
                new Notification.Builder(getApplicationContext());
        builder.setContentTitle(entity.getGenre());
        builder.setContentText(entity.getMemo());
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker(getString(R.string.notification_ticker_view_memo));
        builder.setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
