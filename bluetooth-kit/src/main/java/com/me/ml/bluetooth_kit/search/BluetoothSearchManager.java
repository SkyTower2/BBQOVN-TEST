package com.me.ml.bluetooth_kit.search;

import static com.me.ml.bluetooth_kit.Constants.DEVICE_FOUND;
import static com.me.ml.bluetooth_kit.Constants.EXTRA_SEARCH_RESULT;
import static com.me.ml.bluetooth_kit.Constants.SEARCH_CANCEL;
import static com.me.ml.bluetooth_kit.Constants.SEARCH_START;
import static com.me.ml.bluetooth_kit.Constants.SEARCH_STOP;

import android.os.Bundle;

import com.me.ml.bluetooth_kit.connect.response.BleGeneralResponse;
import com.me.ml.bluetooth_kit.search.response.BluetoothSearchResponse;

public class BluetoothSearchManager {

    public static void search(SearchRequest request, final BleGeneralResponse response) {
        BluetoothSearchRequest requestWrapper = new BluetoothSearchRequest(request);
        BluetoothSearchHelper.getInstance().startSearch(requestWrapper, new BluetoothSearchResponse() {
            @Override
            public void onSearchStarted() {
                response.onResponse(SEARCH_START, null);
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_SEARCH_RESULT, device);
                response.onResponse(DEVICE_FOUND, bundle);
            }

            @Override
            public void onSearchStopped() {
                response.onResponse(SEARCH_STOP, null);
            }

            @Override
            public void onSearchCanceled() {
                response.onResponse(SEARCH_CANCEL, null);
            }
        });
    }

    public static void stopSearch() {
        BluetoothSearchHelper.getInstance().stopSearch();
    }
}
