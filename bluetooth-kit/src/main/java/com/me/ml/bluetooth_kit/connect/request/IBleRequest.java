package com.me.ml.bluetooth_kit.connect.request;

import com.me.ml.bluetooth_kit.connect.IBleConnectDispatcher;

/**
 * Created by dingjikerbo on 16/8/25.
 */
public interface IBleRequest {

    void process(IBleConnectDispatcher dispatcher);

    void cancel();
}
