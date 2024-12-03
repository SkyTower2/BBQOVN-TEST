package com.me.ml.bluetooth_kit.connect.request;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattDescriptor;

import com.me.ml.bluetooth_kit.Code;
import com.me.ml.bluetooth_kit.Constants;
import com.me.ml.bluetooth_kit.connect.listener.WriteDescriptorListener;
import com.me.ml.bluetooth_kit.connect.response.BleGeneralResponse;

import java.util.UUID;

public class BleUnnotifyRequest extends BleRequest implements WriteDescriptorListener {

    private UUID mServiceUUID;
    private UUID mCharacterUUID;

    public BleUnnotifyRequest(UUID service, UUID character, BleGeneralResponse response) {
        super(response);
        mServiceUUID = service;
        mCharacterUUID = character;
    }

    @Override
    public void processRequest() {
        switch (getCurrentStatus()) {
            case Constants.STATUS_DEVICE_DISCONNECTED:
                onRequestCompleted(Code.REQUEST_FAILED);
                break;

            case Constants.STATUS_DEVICE_CONNECTED:
                closeNotify();
                break;

            case Constants.STATUS_DEVICE_SERVICE_READY:
                closeNotify();
                break;

            default:
                onRequestCompleted(Code.REQUEST_FAILED);
                break;
        }
    }

    private void closeNotify() {
        if (!setCharacteristicNotification(mServiceUUID, mCharacterUUID, false)) {
            onRequestCompleted(Code.REQUEST_FAILED);
        } else {
            startRequestTiming();
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGattDescriptor descriptor, int status) {
        stopRequestTiming();

        if (status == BluetoothGatt.GATT_SUCCESS) {
            onRequestCompleted(Code.REQUEST_SUCCESS);
        } else {
            onRequestCompleted(Code.REQUEST_FAILED);
        }
    }
}
