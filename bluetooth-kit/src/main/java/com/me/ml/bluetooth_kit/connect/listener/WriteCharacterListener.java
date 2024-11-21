package com.me.ml.bluetooth_kit.connect.listener;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by dingjikerbo on 2016/8/25.
 */
public interface WriteCharacterListener extends GattResponseListener {
    void onCharacteristicWrite(BluetoothGattCharacteristic characteristic, int status, byte[] value);
}
