package com.me.ml.bluetooth_kit;

import com.me.ml.bluetooth_kit.connect.listener.BleConnectStatusListener;
import com.me.ml.bluetooth_kit.connect.listener.BluetoothStateListener;
import com.me.ml.bluetooth_kit.connect.response.BleNotifyResponse;
import com.me.ml.bluetooth_kit.receiver.listener.BluetoothBondListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by liwentian on 2017/1/13.
 */

public class BluetoothClientReceiver {

    private HashMap<String, HashMap<String, List<BleNotifyResponse>>> mNotifyResponses;
    private HashMap<String, List<BleConnectStatusListener>> mConnectStatusListeners;
    private List<BluetoothStateListener> mBluetoothStateListeners;
    private List<BluetoothBondListener> mBluetoothBondListeners;
}
