package com.me.ml.bluetooth_kit.connect.listener;

/**
 * Created by fuhao on 2017/8/24.
 */

public interface RequestMtuListener extends GattResponseListener {
    void onMtuChanged(int mtu, int status);
}
