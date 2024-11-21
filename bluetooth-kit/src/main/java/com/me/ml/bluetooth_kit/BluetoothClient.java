package com.me.ml.bluetooth_kit;

import android.content.Context;

import com.me.ml.bluetooth_kit.connect.listener.BleConnectStatusListener;
import com.me.ml.bluetooth_kit.connect.listener.BluetoothStateListener;
import com.me.ml.bluetooth_kit.connect.options.BleConnectOptions;
import com.me.ml.bluetooth_kit.connect.response.BleConnectResponse;
import com.me.ml.bluetooth_kit.connect.response.BleMtuResponse;
import com.me.ml.bluetooth_kit.connect.response.BleNotifyResponse;
import com.me.ml.bluetooth_kit.connect.response.BleReadResponse;
import com.me.ml.bluetooth_kit.connect.response.BleReadRssiResponse;
import com.me.ml.bluetooth_kit.connect.response.BleUnnotifyResponse;
import com.me.ml.bluetooth_kit.connect.response.BleWriteResponse;
import com.me.ml.bluetooth_kit.receiver.listener.BluetoothBondListener;
import com.me.ml.bluetooth_kit.search.SearchRequest;
import com.me.ml.bluetooth_kit.search.response.SearchResponse;
import com.me.ml.bluetooth_kit.utils.BluetoothUtils;
import com.me.ml.bluetooth_kit.utils.ByteUtils;
import com.me.ml.bluetooth_kit.utils.proxy.ProxyUtils;
import com.me.ml.utils.log.KLog;

import java.util.UUID;

public class BluetoothClient implements IBluetoothClient {

    private IBluetoothClient mClient;

    public BluetoothClient(Context context) {
        if (context == null) {
            throw new NullPointerException("Context null");
        }
        mClient = BluetoothClientImpl.getInstance(context);
    }

    public void connect(String mac, BleConnectResponse response) {
        connect(mac, null, response);
    }

    @Override
    public void connect(String mac, BleConnectOptions options, BleConnectResponse response) {
        KLog.w(String.format(" 连接设备地址 connect %s", mac));
        response = ProxyUtils.getUIProxy(response);
        mClient.connect(mac, options, response);
    }

    @Override
    public void disconnect(String mac) {
        KLog.w(String.format(" 断开连接设备地址 disconnect %s", mac));

        mClient.disconnect(mac);
    }

    @Override
    public void read(String mac, UUID service, UUID character, BleReadResponse response) {
        KLog.d(String.format("read character for %s: service = %s, character = %s", mac, service, character));

        response = ProxyUtils.getUIProxy(response);
        mClient.read(mac, service, character, response);
    }

    @Override
    public void write(String mac, UUID service, UUID character, byte[] value, BleWriteResponse response) {
        KLog.d(String.format("write character for %s: service = %s, character = %s, value = %s",
                mac, service, character, ByteUtils.byteToString(value)));

        response = ProxyUtils.getUIProxy(response);
        mClient.write(mac, service, character, value, response);
    }

    @Override
    public void readDescriptor(String mac, UUID service, UUID character, UUID descriptor, BleReadResponse response) {
        KLog.d(String.format("readDescriptor for %s: service = %s, character = %s", mac, service, character));

        response = ProxyUtils.getUIProxy(response);
        mClient.readDescriptor(mac, service, character, descriptor, response);
    }

    @Override
    public void writeDescriptor(String mac, UUID service, UUID character, UUID descriptor, byte[] value, BleWriteResponse response) {
        KLog.d(String.format("writeDescriptor for %s: service = %s, character = %s", mac, service, character));

        response = ProxyUtils.getUIProxy(response);
        mClient.writeDescriptor(mac, service, character, descriptor, value, response);
    }

    @Override
    public void writeNoRsp(String mac, UUID service, UUID character, byte[] value, BleWriteResponse response) {
        KLog.d(String.format("writeNoRsp %s: service = %s, character = %s, value = %s", mac, service, character, ByteUtils.byteToString(value)));

        response = ProxyUtils.getUIProxy(response);
        mClient.writeNoRsp(mac, service, character, value, response);
    }

    @Override
    public void notify(String mac, UUID service, UUID character, BleNotifyResponse response) {
        KLog.d(String.format("notify %s: service = %s, character = %s", mac, service, character));

        response = ProxyUtils.getUIProxy(response);
        mClient.notify(mac, service, character, response);
    }

    @Override
    public void unnotify(String mac, UUID service, UUID character, BleUnnotifyResponse response) {
        KLog.d(String.format("unnotify %s: service = %s, character = %s", mac, service, character));

        response = ProxyUtils.getUIProxy(response);
        mClient.unnotify(mac, service, character, response);
    }

    @Override
    public void indicate(String mac, UUID service, UUID character, BleNotifyResponse response) {
        KLog.d(String.format("indicate %s: service = %s, character = %s", mac, service, character));

        response = ProxyUtils.getUIProxy(response);
        mClient.indicate(mac, service, character, response);
    }

    @Override
    public void unindicate(String mac, UUID service, UUID character, BleUnnotifyResponse response) {
        KLog.d(String.format("indicate %s: service = %s, character = %s", mac, service, character));

        response = ProxyUtils.getUIProxy(response);
        mClient.unindicate(mac, service, character, response);
    }

    @Override
    public void readRssi(String mac, BleReadRssiResponse response) {
        KLog.d(String.format("readRssi %s", mac));

        response = ProxyUtils.getUIProxy(response);
        mClient.readRssi(mac, response);
    }

    @Override
    public void requestMtu(String mac, int mtu, BleMtuResponse response) {
        response = ProxyUtils.getUIProxy(response);
        mClient.requestMtu(mac, mtu, response);
    }

    @Override
    public void search(SearchRequest request, SearchResponse response) {
        response = ProxyUtils.getUIProxy(response);
        mClient.search(request, response);
    }

    @Override
    public void stopSearch() {
        mClient.stopSearch();
    }

    @Override
    public void registerConnectStatusListener(String mac, BleConnectStatusListener listener) {
        mClient.registerConnectStatusListener(mac, listener);
    }

    @Override
    public void unregisterConnectStatusListener(String mac, BleConnectStatusListener listener) {
        mClient.unregisterConnectStatusListener(mac, listener);
    }

    @Override
    public void registerBluetoothStateListener(BluetoothStateListener listener) {
        mClient.registerBluetoothStateListener(listener);
    }

    @Override
    public void unregisterBluetoothStateListener(BluetoothStateListener listener) {
        mClient.unregisterBluetoothStateListener(listener);
    }

    @Override
    public void registerBluetoothBondListener(BluetoothBondListener listener) {
        mClient.registerBluetoothBondListener(listener);
    }

    @Override
    public void unregisterBluetoothBondListener(BluetoothBondListener listener) {
        mClient.unregisterBluetoothBondListener(listener);
    }

    public int getConnectStatus(String mac) {
        return BluetoothUtils.getConnectStatus(mac);
    }

    public boolean isBluetoothOpened() {
        return BluetoothUtils.isBluetoothEnabled();
    }

    public boolean openBluetooth() {
        return BluetoothUtils.openBluetooth();
    }

    public boolean closeBluetooth() {
        return BluetoothUtils.closeBluetooth();
    }

    public boolean isBleSupported() {
        return BluetoothUtils.isBleSupported();
    }

    public int getBondState(String mac) {
        return BluetoothUtils.getBondState(mac);
    }

    @Override
    public void clearRequest(String mac, int type) {
        mClient.clearRequest(mac, type);
    }

    @Override
    public void refreshCache(String mac) {
        mClient.refreshCache(mac);
    }
}
