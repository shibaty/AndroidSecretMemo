/**
 * メモリストFragment.
 * @author shibaty
 */
package com.shibaty.secretmemo;

import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.shibaty.secretmemo.db.MemoEntity;
import com.shibaty.secretmemo.util.LogUtil;

/**
 * メモリストFragmentクラス.
 * @author shibaty
 */
public class MemoListFragment extends ListFragment {

    /**
     * FragmentのView作成処理.
     * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (LogUtil.isDebugable()) {
            LogUtil.methodStart();
        }

        // レイアウト設定
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundResource(android.R.color.background_dark);

        if (LogUtil.isDebugable()) {
            LogUtil.methodEnd();
        }
        return view;
    }

    /**
     * FragmentのView作成後処理.
     * @see android.app.ListFragment#onViewCreated(android.view.View, android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (LogUtil.isDebugable()) {
            LogUtil.methodStart();
        }

        super.onViewCreated(view, savedInstanceState);

        // アダプターを設定
        MemoListAdapter adapter = new MemoListAdapter(getActivity());
        setListAdapter(adapter);

        // リストが空の場合のメッセージを設定
        setEmptyText(getResources().getText(R.string.list_empty_message));

        // ContextMenuを登録
        registerForContextMenu(getListView());

        // Fragment固有のメニュー設定を持つことを指示（onCreateOptionsMenuがCallされる）
        setHasOptionsMenu(true);

        if (LogUtil.isDebugable()) {
            LogUtil.methodEnd();
        }
    }

    /**
     * Fragment再開時の処理.
     * @see android.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        if (LogUtil.isDebugable()) {
            LogUtil.methodStart();
        }

        super.onResume();

        // リストをクリアしてデータを表示
        List<MemoEntity> list = ((MainActivity)getActivity()).getAllItem();
        ((MemoListAdapter)getListAdapter()).clear();
        for (MemoEntity entity : list) {
            ((MemoListAdapter)getListAdapter()).add(entity);
        }

        if (LogUtil.isDebugable()) {
            LogUtil.methodEnd();
        }
    }

    /**
     * リストアイテムをクリックしたときの処理.
     * @see android.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (LogUtil.isDebugable()) {
            LogUtil.methodStart();
        }

        if (!((MemoListAdapter)getListAdapter()).isSelectMode()) {
            MemoEntity entity = (MemoEntity)l.getItemAtPosition(position);
            ((MainActivity)getActivity()).tapItem(entity);
        }

        if (LogUtil.isDebugable()) {
            LogUtil.methodEnd();
        }
    }

    /**
     * OptionsMenu作成.
     * @see android.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (LogUtil.isDebugable()) {
            LogUtil.methodStart();
        }

        if (((MemoListAdapter)getListAdapter()).isSelectMode()) {
            inflater.inflate(R.menu.list_selectmode, menu);
        } else {
            inflater.inflate(R.menu.list, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);

        if (LogUtil.isDebugable()) {
            LogUtil.methodEnd();
        }
    }

    /**
     * OptionsMenuのアイテム選択時の処理.
     * @see android.app.Fragment#onOptionsItemSelected(android.view.MenuItem)
     * @param item {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (LogUtil.isDebugable()) {
            LogUtil.methodStart();
        }
        boolean result = true;

        switch (item.getItemId()) {
        case R.id.menu_add:
            ((MainActivity)getActivity()).moveToItemEditFragment(new MemoEntity());
            break;
        case R.id.menu_delete_as:
            getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            ((MemoListAdapter)getListAdapter()).setSelectMode(true);
            getFragmentManager().invalidateOptionsMenu();
            break;
        case R.id.menu_delete:
            SparseBooleanArray list = getListView().getCheckedItemPositions();
            for (int i = list.size() - 1; i >= 0; i--) {
                MemoEntity entity = (MemoEntity)getListView().getItemAtPosition(list.keyAt(i));
                ((MainActivity)getActivity()).deleteItem(entity);
                ((MemoListAdapter)getListAdapter()).remove(entity);
            }
            // FALLTHROUGH
        case R.id.menu_cancel:
            getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
            ((MemoListAdapter)getListAdapter()).setSelectMode(false);
            getFragmentManager().invalidateOptionsMenu();
            break;
        default:
            result = super.onOptionsItemSelected(item);
            break;
        }
        if (LogUtil.isDebugable()) {
            LogUtil.methodEnd();
        }
        return result;
    }

    /**
     * ContextMenu生成.
     * @see android.app.Fragment#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.listcontext, menu);
    }

    /**
     * ContextMenuのアイテム選択時の処理.
     * @see android.app.Fragment#onContextItemSelected(android.view.MenuItem)
     * @return 処理結果
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean result = false;

        AdapterContextMenuInfo acmi = (AdapterContextMenuInfo)item.getMenuInfo();

        MemoEntity entity = (MemoEntity)getListView().getItemAtPosition(acmi.position);

        switch (item.getItemId()) {
        case R.id.menu_copy:
            ((MainActivity)getActivity()).copyItem(entity);
            result = true;
            break;
        case R.id.menu_notification:
            ((MainActivity)getActivity()).notificationItem(entity);
            result = true;
            break;
        case R.id.menu_edit:
            ((MainActivity)getActivity()).moveToItemEditFragment(entity);
            result = true;
            break;
        case R.id.menu_delete:
            ((MainActivity)getActivity()).deleteItem(entity);
            ((MemoListAdapter)getListAdapter()).remove(entity);
            result = true;
            break;
        default:
            result = super.onContextItemSelected(item);
            break;
        }
        return result;
    }
}
