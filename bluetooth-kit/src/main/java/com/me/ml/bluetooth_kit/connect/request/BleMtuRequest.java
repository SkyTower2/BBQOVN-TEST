package com.me.ml.bluetooth_kit.connect.request;

import android.bluetooth.BluetoothGatt;

import com.me.ml.bluetooth_kit.Code;
import com.me.ml.bluetooth_kit.Constants;
import com.me.ml.bluetooth_kit.connect.listener.RequestMtuListener;
import com.me.ml.bluetooth_kit.connect.response.BleGeneralResponse;

public class BleMtuRequest extends BleRequest implements RequestMtuListener {

    private int mMtu;

    public BleMtuRequest(int mtu, BleGeneralResponse response) {
        super(response);
        mMtu = mtu;
    }

    @Override
    public void processRequest() {
        switch (getCurrentStatus()) {
            case Constants.STATUS_DEVICE_DISCONNECTED:
                onRequestCompleted(Code.REQUEST_FAILED);
                break;

            case Constants.STATUS_DEVICE_CONNECTED:
                requestMtu();
                break;

            case Constants.STATUS_DEVICE_SERVICE_READY:
                requestMtu();
                break;

            default:
                onRequestCompleted(Code.REQUEST_FAILED);
                break;
        }
    }

    private void requestMtu() {
        if (!requestMtu(mMtu)) {
            onRequestCompleted(Code.REQUEST_FAILED);
        } else {
            startRequestTiming();
        }
    }

    @Override
    public void onMtuChanged(int mtu, int status) {
        stopRequestTiming();

        if (status == BluetoothGatt.GATT_SUCCESS) {
            putIntExtra(Constants.EXTRA_MTU, mtu);
            onRequestCompleted(Code.REQUEST_SUCCESS);
        } else {
            onRequestCompleted(Code.REQUEST_FAILED);
        }
    }
}
