package com.me.ml.bluetooth_kit.receiver;

import com.me.ml.bluetooth_kit.receiver.listener.BluetoothReceiverListener;

import java.util.List;

public interface IReceiverDispatcher {

    List<BluetoothReceiverListener> getListeners(Class<?> clazz);
}
