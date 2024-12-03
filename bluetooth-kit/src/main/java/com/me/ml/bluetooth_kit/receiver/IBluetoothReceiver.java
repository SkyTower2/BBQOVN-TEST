package com.me.ml.bluetooth_kit.receiver;

import com.me.ml.bluetooth_kit.receiver.listener.BluetoothReceiverListener;

public interface IBluetoothReceiver {

    void register(BluetoothReceiverListener listener);
}
