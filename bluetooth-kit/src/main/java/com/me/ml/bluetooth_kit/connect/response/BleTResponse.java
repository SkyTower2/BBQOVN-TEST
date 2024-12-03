package com.me.ml.bluetooth_kit.connect.response;

public interface BleTResponse<T> {
    void onResponse(int code, T data);
}
