/**
 * メモアイテムアダプター.
 * @author shibaty
 */

package com.shibaty.secretmemo;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.shibaty.secretmemo.db.MemoEntity;

/**
 * メモアイテムのアダプタークラス.
 *
 * @author shibaty
 */
public class MemoListAdapter extends ArrayAdapter<MemoEntity> {

    /** LayoutInflater. */
    private LayoutInflater inflater;
    /** 選択モード. */
    private boolean isSelectMode;

    /**
     * コンストラクタ.
     * @param context コンテキスト
     */
    public MemoListAdapter(Context context) {
        super(context, R.layout.listitem_memo);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 指定された位置のアイテムのViewを設定.
     *
     * @see android.widget.Adapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Viewのインスタンスがあれば再利用する
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem_memo, parent, false);
            holder = new ViewHolder();
            holder.genre = (TextView) convertView.findViewById(R.id.list_item_textview_genre);
            holder.memo = (CheckedTextView) convertView.findViewById(R.id.list_item_textview_memo);
            holder.createtime = (TextView) convertView
                    .findViewById(R.id.list_item_textview_createtime);
            holder.updatetime = (TextView) convertView
                    .findViewById(R.id.list_item_textview_updatetime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Viewを設定
        MemoEntity entity = getItem(position);
        holder.genre.setText(entity.getGenre());
        holder.memo.setText(setDotString(entity.getMemo()));
        if (isSelectMode) {
            int[] attr = {
                android.R.attr.listChoiceIndicatorMultiple
            };
            holder.memo.setCheckMarkDrawable(convertView.getContext().getTheme()
                    .obtainStyledAttributes(attr).getDrawable(0));
            holder.memo.setChecked(((ListView) parent).isItemChecked(position));
        } else {
            holder.memo.setCheckMarkDrawable(null);
            holder.memo.setChecked(false);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        holder.createtime.setText(sdf.format(entity.getCreatetime()));
        holder.updatetime.setText(sdf.format(entity.getUpdatetime()));

        return convertView;
    }

    /**
     * Viewの構造を保持するクラス.
     *
     * @author shibaty
     */
    private static class ViewHolder {
        /** ジャンル. */
        TextView genre;
        /** メモ内容. */
        CheckedTextView memo;
        /** 作成日. */
        TextView createtime;
        /** 更新日. */
        TextView updatetime;
    }

    /**
     * 選択モードかどうかを返却.
     *
     * @return isSelectMode
     */
    public boolean isSelectMode() {
        return isSelectMode;
    }

    /**
     * 選択モードを設定/解除.
     *
     * @param isSelectMode 選択モード
     */
    public void setSelectMode(boolean isSelectMode) {
        this.isSelectMode = isSelectMode;
        notifyDataSetChanged();
    }

    /**
     * 指定された文字列の文字数分の*を返却.
     *
     * @param str 文字列
     * @return *に変換された文字列
     */
    private String setDotString(String str) {
        String result_str = "";
        for (int i = 0; i < str.length(); i++) {
            result_str += "*";
        }
        return result_str;
    }
}
