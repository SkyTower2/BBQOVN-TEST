package com.me.ml.bluetooth_kit.connect.listener;

import com.me.ml.bluetooth_kit.model.BleGattProfile;

/**
 * Created by dingjikerbo on 2016/8/25.
 */
public interface ServiceDiscoverListener extends GattResponseListener {
    void onServicesDiscovered(int status, BleGattProfile profile);
}
