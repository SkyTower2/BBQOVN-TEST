package com.me.ml.bluetooth_kit.connect.listener;

import android.bluetooth.BluetoothGattCharacteristic;

public interface ReadCharacterListener extends GattResponseListener {
    void onCharacteristicRead(BluetoothGattCharacteristic characteristic, int status, byte[] value);
}
