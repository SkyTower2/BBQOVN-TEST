package com.me.ml.bluetooth_kit.receiver;

import com.me.ml.bluetooth_kit.receiver.listener.BluetoothReceiverListener;

/**
 * Created by dingjikerbo on 2016/11/25.
 */

public interface IBluetoothReceiver {

    void register(BluetoothReceiverListener listener);
}
