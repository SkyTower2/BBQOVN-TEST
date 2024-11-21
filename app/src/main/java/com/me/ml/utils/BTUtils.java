package com.me.ml.utils;

import android.bluetooth.BluetoothAdapter;

/**
 * @author ml
 * @date 2024/10/11 9:49
 */
public class BTUtils {

    /**
     * 判断蓝牙是否不可用
     *
     * @return true：不可用；false：可用
     */
    public static boolean isDisabled() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            return false;
        }
        return !adapter.isEnabled();
    }
}
