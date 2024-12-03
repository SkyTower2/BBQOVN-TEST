package com.me.ml.bluetooth_kit.connect.response;

import java.util.UUID;

public interface BleNotifyResponse extends BleResponse {

    void onNotify(UUID service, UUID character, byte[] value);
}
