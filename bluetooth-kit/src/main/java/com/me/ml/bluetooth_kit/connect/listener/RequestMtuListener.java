package com.me.ml.bluetooth_kit.connect.listener;

public interface RequestMtuListener extends GattResponseListener {
    void onMtuChanged(int mtu, int status);
}
