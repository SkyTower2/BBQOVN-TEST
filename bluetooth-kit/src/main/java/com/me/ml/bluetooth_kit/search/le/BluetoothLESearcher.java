package com.me.ml.bluetooth_kit.search.le;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.os.Build;

import com.me.ml.bluetooth_kit.search.BluetoothSearcher;
import com.me.ml.bluetooth_kit.search.SearchResult;
import com.me.ml.bluetooth_kit.search.response.BluetoothSearchResponse;
import com.me.ml.bluetooth_kit.utils.BluetoothLog;
import com.me.ml.bluetooth_kit.utils.BluetoothUtils;

public class BluetoothLESearcher extends BluetoothSearcher {

	private BluetoothLESearcher() {
		mBluetoothAdapter = BluetoothUtils.getBluetoothAdapter();
	}

	public static BluetoothLESearcher getInstance() {
		return BluetoothLESearcherHolder.instance;
	}

	private static class BluetoothLESearcherHolder {
		private static BluetoothLESearcher instance = new BluetoothLESearcher();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@SuppressWarnings("deprecation")
	@Override
	public void startScanBluetooth(BluetoothSearchResponse response) {
		// TODO Auto-generated method stub
		super.startScanBluetooth(response);
		
		mBluetoothAdapter.startLeScan(mLeScanCallback);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@SuppressWarnings("deprecation")
	@Override
	public void stopScanBluetooth() {
		// TODO Auto-generated method stub
		try {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		} catch (Exception e) {
			BluetoothLog.e(e);
		}

		super.stopScanBluetooth();
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@SuppressWarnings("deprecation")
	@Override
	protected void cancelScanBluetooth() {
		// TODO Auto-generated method stub
		mBluetoothAdapter.stopLeScan(mLeScanCallback);
		super.cancelScanBluetooth();
	}

	private final LeScanCallback mLeScanCallback = new LeScanCallback() {

		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			// TODO Auto-generated method stub
            notifyDeviceFounded(new SearchResult(device, rssi, scanRecord));
		}
		
	};
}
