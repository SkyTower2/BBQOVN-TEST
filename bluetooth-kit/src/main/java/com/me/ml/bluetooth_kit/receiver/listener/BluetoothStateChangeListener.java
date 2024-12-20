package com.me.ml.bluetooth_kit.receiver.listener;

import com.me.ml.bluetooth_kit.BluetoothClientImpl;
import com.me.ml.bluetooth_kit.Constants;

public abstract class BluetoothStateChangeListener extends BluetoothReceiverListener {

    protected abstract void onBluetoothStateChanged(int prevState, int curState);

    @Override
    public void onInvoke(Object... args) {
        int prevState = (int) args[0];
        int curState = (int) args[1];

        if (curState == Constants.STATE_OFF || curState == Constants.STATE_TURNING_OFF) {
            BluetoothClientImpl.getInstance(null).stopSearch();
        }

        onBluetoothStateChanged(prevState, curState);
    }

    @Override
    public String getName() {
        return BluetoothStateChangeListener.class.getSimpleName();
    }
}
