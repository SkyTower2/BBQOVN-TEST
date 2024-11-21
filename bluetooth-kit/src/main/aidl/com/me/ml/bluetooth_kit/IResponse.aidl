// IBleResponse.aidl
package com.me.ml.bluetooth_kit;

// Declare any non-default types here with import statements

interface IResponse {
    void onResponse(int code, inout Bundle data);
}
