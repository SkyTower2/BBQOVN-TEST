package com.me.ml.bluetooth_kit.search.response;

import com.me.ml.bluetooth_kit.search.SearchResult;

public interface SearchResponse {

    void onSearchStarted();

    void onDeviceFounded(SearchResult device);

    void onSearchStopped();

    void onSearchCanceled();
}
