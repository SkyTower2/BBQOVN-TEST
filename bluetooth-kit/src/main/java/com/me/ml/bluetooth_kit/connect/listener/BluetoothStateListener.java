package com.me.ml.bluetooth_kit.connect.listener;

import com.me.ml.bluetooth_kit.receiver.listener.BluetoothClientListener;

public abstract class BluetoothStateListener extends BluetoothClientListener {

    public abstract void onBluetoothStateChanged(boolean openOrClosed);

    @Override
    public void onSyncInvoke(Object... args) {
        boolean openOrClosed = (boolean) args[0];
        onBluetoothStateChanged(openOrClosed);
    }
}
