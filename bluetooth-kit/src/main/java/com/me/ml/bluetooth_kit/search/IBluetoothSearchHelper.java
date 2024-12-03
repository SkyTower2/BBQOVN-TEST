package com.me.ml.bluetooth_kit.search;

import com.me.ml.bluetooth_kit.search.response.BluetoothSearchResponse;

public interface IBluetoothSearchHelper {

    void startSearch(BluetoothSearchRequest request, BluetoothSearchResponse response);

    void stopSearch();
}
