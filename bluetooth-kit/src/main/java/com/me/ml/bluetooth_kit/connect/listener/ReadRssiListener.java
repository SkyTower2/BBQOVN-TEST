package com.me.ml.bluetooth_kit.connect.listener;

public interface ReadRssiListener extends GattResponseListener {
    void onReadRemoteRssi(int rssi, int status);
}
