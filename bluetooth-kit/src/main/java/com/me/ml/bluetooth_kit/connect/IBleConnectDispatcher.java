package com.me.ml.bluetooth_kit.connect;

import com.me.ml.bluetooth_kit.connect.request.BleRequest;

public interface IBleConnectDispatcher {

    void onRequestCompleted(BleRequest request);
}
