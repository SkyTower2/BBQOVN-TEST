package com.me.ml.bluetooth_kit.connect.request;

import com.me.ml.bluetooth_kit.Code;
import com.me.ml.bluetooth_kit.connect.response.BleGeneralResponse;

public class BleRefreshCacheRequest extends BleRequest {

    public BleRefreshCacheRequest(BleGeneralResponse response) {
        super(response);
    }

    @Override
    public void processRequest() {
        refreshDeviceCache();

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                onRequestCompleted(Code.REQUEST_SUCCESS);
            }
        }, 3000);
    }
}
