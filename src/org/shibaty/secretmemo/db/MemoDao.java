
package org.shibaty.secretmemo.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.shibaty.secretmemo.util.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Memoアイテム DAOクラス.<br>
 *
 * @author shibaty
 */
public class MemoDao extends SQLiteOpenHelper {
    // TODO コメント
    /**
     * DB名.<br>
     */
    private static final String DBNAME = "secretmemo.db";

    /**
     * DB Version.<br>
     */
    private static final int DBVERSION = 1;

    /**
     * DDL File Name.<br>
     */
    private static final String CREATE_SQL_FILENAME = "sql/create/memo.sql";

    /**
     * Table Name.<br>
     */
    private static final String TABLE_NAME = "memo";

    /**
     * Key - ID.<br>
     */
    private static final String KEY_ID = "id";
    /**
     * Key - Genre.<br>
     */
    private static final String KEY_GENRE = "genre";
    /**
     * Key - Memo.<br>
     */
    private static final String KEY_MEMO = "memo";
    /**
     * Key - Create Time.<br>
     */
    private static final String KEY_CREATETIME = "createtime";

    /**
     * Key - Update Time.<br>
     */
    private static final String KEY_UPDATETIME = "updatetime";

    /**
     * SQLite Databese.<br>
     */
    private SQLiteDatabase mDb;

    /**
     * Asset Manager.<br>
     */
    private AssetManager assetmanager;

    /**
     * コンストラクタ.<br>
     *
     * @param context Context
     */
    public MemoDao(Context context) {
        super(context, DBNAME, null, DBVERSION);
        assetmanager = context.getAssets();
    }

    /**
     * Dao生成時の処理.<br>
     *
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(
     *      android.database.sqlite.SQLiteDatabase)
     * @param db {@inheritDoc}
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        if (LogUtil.isDebugable()) {
            LogUtil.methodStart();
        }

        InputStream is = null;
        try {
            is = assetmanager.open(CREATE_SQL_FILENAME);
            byte[] buf = new byte[is.available()];
            is.read(buf);
            String sql = new String(buf);
            is.close();
            db.execSQL(sql);
        } catch (IOException ioe) {
            // nop
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                    // nop
                }
            }
        }

        if (LogUtil.isDebugable()) {
            LogUtil.methodEnd();
        }
    }

    /**
     * DBバージョン更新時の処理.<br>
     *
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(
     *      android.database.sqlite.SQLiteDatabase, int, int)
     * @param db {@inheritDoc}
     * @param oldVersion {@inheritDoc}
     * @param newVersion {@inheritDoc}
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // nop
    }

    /**
     * DBの初期化.<br>
     */
    public void init() {
        mDb = getWritableDatabase();
    }

    /**
     * DBのクローズ.<br>
     */
    public void finish() {
        mDb.close();
    }

    /**
     * すべて取得.<br>
     *
     * @return メモのリスト
     */
    public List<MemoEntity> getAll() {
        List<MemoEntity> list = new ArrayList<MemoEntity>();

        Cursor cursor = mDb.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            MemoEntity entity = new MemoEntity();

            String[] columns = cursor.getColumnNames();
            for (String column : columns) {
                int idx = cursor.getColumnIndex(column);

                if (column.equalsIgnoreCase(KEY_ID)) {
                    entity.setId(cursor.getInt(idx));
                } else if (column.equalsIgnoreCase(KEY_GENRE)) {
                    entity.setGenre(cursor.getString(idx));
                } else if (column.equalsIgnoreCase(KEY_MEMO)) {
                    entity.setMemo(cursor.getString(idx));
                } else if (column.equalsIgnoreCase(KEY_CREATETIME)) {
                    entity.setCreatetime(new Date(cursor.getLong(idx)));
                } else if (column.equalsIgnoreCase(KEY_UPDATETIME)) {
                    entity.setUpdatetime(new Date(cursor.getLong(idx)));
                }
            }
            list.add(entity);
        }
        cursor.close();

        return list;
    }

    /**
     * メモの追加.<br>
     *
     * @param entity メモ
     * @return 処理結果
     */
    public long insert(MemoEntity entity) {
        Date date = new Date();
        entity.setCreatetime(date);
        entity.setUpdatetime(date);

        ContentValues cv = new ContentValues();
        // ID is autoincrement
        // cv.put(KEY_ID, entity.getId());
        cv.put(KEY_GENRE, entity.getGenre());
        cv.put(KEY_MEMO, entity.getMemo());
        cv.put(KEY_CREATETIME, entity.getCreatetime().getTime());
        cv.put(KEY_UPDATETIME, entity.getUpdatetime().getTime());

        return mDb.insert(TABLE_NAME, null, cv);
    }

    /**
     * メモの更新.<br>
     *
     * @param entity メモ
     * @return 処理結果
     */
    public int update(MemoEntity entity) {
        Date date = new Date();
        entity.setUpdatetime(date);

        ContentValues cv = new ContentValues();
        cv.put(KEY_GENRE, entity.getGenre());
        cv.put(KEY_MEMO, entity.getMemo());
        cv.put(KEY_UPDATETIME, entity.getUpdatetime().getTime());

        return mDb.update(TABLE_NAME, cv, KEY_ID + " = ?",
                new String[] {
                    String.valueOf(entity.getId())
                });
    }

    /**
     * メモの削除.<br>
     *
     * @param entity メモ
     * @return 処理結果
     */
    public int delete(MemoEntity entity) {
        return mDb.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] {
                    String.valueOf(entity.getId())
                });
    }
}
