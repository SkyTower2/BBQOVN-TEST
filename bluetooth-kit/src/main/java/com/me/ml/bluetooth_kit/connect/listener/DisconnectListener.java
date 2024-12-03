package com.me.ml.bluetooth_kit.connect.listener;

public interface DisconnectListener extends GattResponseListener {
    void onDisconnected();
}
