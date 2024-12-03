package com.me.ml.bluetooth_kit.connect.request;

import com.me.ml.bluetooth_kit.connect.IBleConnectDispatcher;

public interface IBleRequest {

    void process(IBleConnectDispatcher dispatcher);

    void cancel();
}
