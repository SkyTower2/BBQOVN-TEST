package com.me.ml.logcat;

import android.app.Activity;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;


import com.hjq.window.EasyWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 列表选择类
 */
final class ChooseWindow extends EasyWindow<ChooseWindow> implements
        AdapterView.OnItemClickListener, EasyWindow.OnClickListener<View> {

    private final ChooseAdapter mAdapter;
    private OnListener mListener;

    ChooseWindow(Activity activity) {
        super(activity);
        setContentView(R.layout.logcat_window_choose);
        setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置沉浸式状态栏
            addWindowFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        removeWindowFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        ListView listView = findViewById(R.id.lv_choose_list);
        mAdapter = new ChooseAdapter();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        setOnClickListener(R.id.fl_choose_root, this);
    }

    @Override
    public void onClick(EasyWindow window, View view) {
        cancel();
    }

    ChooseWindow setList(int... stringIds) {
        List<String> data = new ArrayList<>();
        for (int stringId : stringIds) {
            data.add(getContext().getResources().getString(stringId));
        }
        return setList(data);
    }

    ChooseWindow setList(String... data) {
        return setList(Arrays.asList(data));
    }

    ChooseWindow setList(List<String> data) {
        mAdapter.setData(data);
        return this;
    }

    ChooseWindow setListener(OnListener listener) {
        mListener = listener;
        return this;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListener != null) {
            mListener.onSelected(position);
        }
        cancel();
    }

    public interface OnListener {

        void onSelected(int position);
    }
}