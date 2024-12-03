package com.me.ml.bluetooth_kit.utils.hook;

import com.me.ml.bluetooth_kit.utils.BluetoothLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class BluetoothGattProxyHandler implements InvocationHandler {

    private Object bluetoothGatt;

    BluetoothGattProxyHandler(Object bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        BluetoothLog.v(String.format("IBluetoothGatt method: %s", method.getName()));
        return method.invoke(bluetoothGatt, args);
    }
}
