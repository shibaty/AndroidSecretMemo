/**
 * アイテム編集フラグメント.
 */

package com.shibaty.secretmemo;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.shibaty.secretmemo.db.MemoEntity;

/**
 * アイテム編集フラグメントクラス.
 *
 * @author shibaty
 */
public class ItemEditFragment extends Fragment {

    public static final String ARGKEY_ENTITY = "ARGKEY_ENTITY";

    /** メモ. */
    private MemoEntity entity;

    /** View - ジャンル. */
    private TextView genre;
    /** View - メモ内容. */
    private TextView memo;

    /**
     * FragmentのView作成処理.
     *
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
     *      android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // レイアウト設定
        View view = inflater.inflate(R.layout.fragment_itemedit, container, false);
        view.setBackgroundResource(android.R.color.background_dark);

        // ボタンのアクション設定
        Button cancelButton = (Button) view.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button okButton = (Button) view.findViewById(R.id.button_ok);
        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntity();
                finish();
            }
        });

        // Entityをパラメータから取得する
        Bundle bundle = getArguments();
        entity = (MemoEntity) bundle.get(ARGKEY_ENTITY);

        // 各Viewの保持
        genre = (TextView) view.findViewById(R.id.edittext_genre);
        memo = (TextView) view.findViewById(R.id.edittext_memo);

        // 各Viewに値を設定
        genre.setText(entity.getGenre());
        memo.setText(entity.getMemo());

        return view;
    }

    /**
     * ソフトキーボードを閉じる.
     */
    private void closeSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    /**
     * フラグメントを閉じる.
     */
    private void finish() {
        closeSoftKeyboard();
        getFragmentManager().popBackStack();
    }

    /**
     * Entityを保存.
     */
    private void saveEntity() {
        entity.setGenre(genre.getText().toString());
        entity.setMemo(memo.getText().toString());
        if (entity.getId() == MemoEntity.ID_NONE) {
            ((MainActivity) getActivity()).addItem(entity);
        } else {
            ((MainActivity) getActivity()).updateItem(entity);
        }
    }
}
