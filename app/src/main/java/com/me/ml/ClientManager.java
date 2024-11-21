package com.me.ml;

import com.me.ml.app.AppApplication;
import com.me.ml.bluetooth_kit.BluetoothClient;

/**
 * @author ml
 * @date 2024/9/07
 */
public class ClientManager {

    private static BluetoothClient mClient;

    public static BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(AppApplication.getInstance());
                }
            }
        }
        return mClient;
    }
}
