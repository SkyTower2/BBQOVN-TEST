// IBluetoothManager.aidl
package com.me.ml.bluetooth_kit;

// Declare any non-default types here with import statements

import com.me.ml.bluetooth_kit.IResponse;

interface IBluetoothService {
    void callBluetoothApi(int code, inout Bundle args, IResponse response);
}
