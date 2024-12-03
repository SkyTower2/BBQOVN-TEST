package com.me.ml.bluetooth_kit.connect.listener;

import com.me.ml.bluetooth_kit.model.BleGattProfile;

public interface ServiceDiscoverListener extends GattResponseListener {
    void onServicesDiscovered(int status, BleGattProfile profile);
}
