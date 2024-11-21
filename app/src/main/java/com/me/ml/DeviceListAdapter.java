package com.me.ml;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.me.ml.utils.CommonUtils;
import com.me.ml.ble.BluetoothDataCollectorActivity;
import com.me.ml.bluetooth_kit.beacon.Beacon;
import com.me.ml.bluetooth_kit.search.SearchResult;
import com.me.ml.repository.DeviceSearchRepository;
import com.oyml.bluetooth.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DeviceListAdapter extends BaseAdapter implements Comparator<SearchResult> {

    private Context mContext;

    private List<SearchResult> mDataList;

    public DeviceListAdapter(Context context) {
        mContext = context;
        mDataList = new ArrayList<SearchResult>();
    }

    public void setDataList(List<SearchResult> datas) {
        mDataList.clear();
        mDataList.addAll(datas);
        mDataList.sort(this);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int compare(SearchResult lhs, SearchResult rhs) {
        return rhs.rssi - lhs.rssi;
    }

    private static class ViewHolder {
        TextView name;
        TextView mac;
        TextView rssi;
        Button collect;
        Button connect;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.device_list_item, null, false);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.mac = (TextView) convertView.findViewById(R.id.mac);
            holder.rssi = (TextView) convertView.findViewById(R.id.rssi);
            holder.collect = (Button) convertView.findViewById(R.id.is_collect_ble);
            holder.connect = convertView.findViewById(R.id.connect_ble); // 连接按钮
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SearchResult result = (SearchResult) getItem(position);

        //设置设备信息
        holder.name.setText(result.getName());
        holder.mac.setText(result.getAddress());
        holder.rssi.setText(String.format("%d dBm", result.rssi));

        //更新收藏状态
        boolean isFavorited = DeviceSearchRepository.getInstance().getFavoriteList().contains(result.getAddress());
        holder.collect.setText(isFavorited ? "已收藏" : "未收藏");
        holder.collect.setBackground(mContext.getResources().getDrawable(isFavorited ? R.drawable.ok_collect_bg_bt : R.drawable.no_collect_bg_bt));

        Beacon beacon = new Beacon(result.scanRecord);

        //点击连接按钮的逻辑
        holder.connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BluetoothDataCollectorActivity.class);
                intent.putExtra("mac", result.getAddress());
                mContext.startActivity(intent);
            }
        });

        //点击收藏按钮的逻辑
        ViewHolder finalHolder = holder;
        holder.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("已收藏".contentEquals(finalHolder.collect.getText())) {
                    finalHolder.collect.setText("未收藏");
                    finalHolder.collect.setBackground(mContext.getResources().getDrawable(R.drawable.no_collect_bg_bt));
                    DeviceSearchRepository.getInstance().removeDeviceFromFavorite(result.getAddress());
                } else {
                    finalHolder.collect.setText("已收藏");
                    finalHolder.collect.setBackground(mContext.getResources().getDrawable(R.drawable.ok_collect_bg_bt));
                    CommonUtils.toast("收藏成功");
                    DeviceSearchRepository.getInstance().addDeviceToFavorite(result.getAddress());
                }
            }
        });

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(mContext, DeviceDetailActivity.class);
//                intent.putExtra("mac", result.getAddress());
//
//                mContext.startActivity(intent);
//            }
//        });

        return convertView;
    }
}
