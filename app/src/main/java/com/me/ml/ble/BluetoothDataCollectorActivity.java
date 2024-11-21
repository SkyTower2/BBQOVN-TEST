package com.me.ml.ble;

import static com.me.ml.bluetooth_kit.Constants.REQUEST_SUCCESS;
import static com.me.ml.bluetooth_kit.Constants.STATUS_CONNECTED;
import static com.me.ml.bluetooth_kit.Constants.STATUS_DISCONNECTED;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.me.ml.ClientManager;
import com.me.ml.DetailItem;
import com.me.ml.bluetooth_kit.connect.listener.BleConnectStatusListener;
import com.me.ml.bluetooth_kit.connect.options.BleConnectOptions;
import com.me.ml.bluetooth_kit.connect.response.BleConnectResponse;
import com.me.ml.bluetooth_kit.connect.response.BleNotifyResponse;
import com.me.ml.bluetooth_kit.connect.response.BleReadResponse;
import com.me.ml.bluetooth_kit.connect.response.BleReadRssiResponse;
import com.me.ml.bluetooth_kit.model.BleGattCharacter;
import com.me.ml.bluetooth_kit.model.BleGattProfile;
import com.me.ml.bluetooth_kit.model.BleGattService;
import com.me.ml.bluetooth_kit.utils.BluetoothUtils;
import com.me.ml.bluetooth_kit.utils.ByteUtils;
import com.me.ml.utils.CommonUtils;
import com.me.ml.utils.log.KLog;
import com.oyml.bluetooth.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * 蓝牙数据采集器
 *
 * @author ml
 * @date 2024/9/10 19:31
 */
public class BluetoothDataCollectorActivity extends Activity {
    private BluetoothDevice mDevice;
    private TextView mTvTitle;
    private TextView mBackBt;
    private ProgressBar mPbar;
    private LinearLayout dataContent;
    private Button connectStatus;
    private boolean mConnected;
    private TextView bleName;
    private TextView rssiValue;
    private TextView batteryVoltage;
    private TextView batteryPercentage;
    private TextView ambientTemp;
    private TextView temp1;
    private TextView temp2;
    private TextView temp3;
    private TextView temp4;
    private TextView temp5;
    private TextView temp6;
    private TextView bleSoftwareVersion;
    private TextView bleHardwareVersion;
    //是否点击断开连接
    private Boolean isClickDisconnect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_data_collector_activity);

        Intent intent = getIntent();
        String mac = intent.getStringExtra("mac");
        //获取蓝牙设备
        mDevice = BluetoothUtils.getRemoteDevice(mac);

        //软件版本号
        bleSoftwareVersion = (TextView) findViewById(R.id.device_software);
        //硬件版本号
        bleHardwareVersion = (TextView) findViewById(R.id.device_hardware);

        //蓝牙名称
        bleName = (TextView) findViewById(R.id.device_name);
        bleName.setText(String.format("蓝牙名称: %s", mDevice.getName()));

        //信号值
        rssiValue = (TextView) findViewById(R.id.rssi_value);
        //电量
        batteryPercentage = (TextView) findViewById(R.id.battery_percentage);
        //电池电压
        batteryVoltage = (TextView) findViewById(R.id.battery_voltage);
        //环境温度
        ambientTemp = (TextView) findViewById(R.id.ambient_temperature);
        //温度1
        temp1 = (TextView) findViewById(R.id.temp_1);
        //温度2
        temp2 = (TextView) findViewById(R.id.temp_2);
        //温度3
        temp3 = (TextView) findViewById(R.id.temp_3);
        //温度4
        temp4 = (TextView) findViewById(R.id.temp_4);
        //温度5
        temp5 = (TextView) findViewById(R.id.temp_5);
        //温度6
        temp6 = (TextView) findViewById(R.id.temp_6);

        dataContent = (LinearLayout) findViewById(R.id.ble_data_list);

        //连接状态按钮
        connectStatus = (Button) findViewById(R.id.connect_status);

        //设置标题
        mTvTitle = (TextView) findViewById(R.id.ble_data_title);
        mTvTitle.setText(mDevice.getAddress());

        //连接加载
        mPbar = (ProgressBar) findViewById(R.id.connect_load);

        //返回按钮
        mBackBt = (TextView) findViewById(R.id.back_bt);
        mBackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //注册连接状态监听器
        ClientManager.getClient().registerConnectStatusListener(mDevice.getAddress(), mConnectStatusListener);

        //连接设备
        connectDeviceIfNeeded();

        //连接状态事件监听
        connectStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mConnected) {
                    CommonUtils.toast("已手动断开设备连接");
                    ClientManager.getClient().disconnect(mDevice.getAddress());
                    connectStatus.setText("重新连接");
                    isClickDisconnect = true;
                } else {
                    KLog.e("重连设备");
                    //重置默认
                    resetDefault();

                    CommonUtils.toast("正在重新连接设备");
                    connectDevice();
                }
            }
        });
    }

    /**
     * 重置默认
     */
    private void resetDefault() {
        rssiValue.setText(String.format("信号值: %s", "---"));
        batteryPercentage.setText(String.format("电量: %s", "---"));
        batteryVoltage.setText(String.format("电池电压: %s", "---"));
        ambientTemp.setText(String.format("环境温度: %s", "---"));
        temp1.setText(String.format("温度1: %s", "---"));
        temp2.setText(String.format("温度2: %s", "---"));
        temp3.setText(String.format("温度3: %s", "---"));
        temp4.setText(String.format("温度4: %s", "---"));
        temp5.setText(String.format("温度5: %s", "---"));
        temp6.setText(String.format("温度6: %s", "---"));
        bleSoftwareVersion.setText(String.format("软件版本号: %s", "---"));
        bleHardwareVersion.setText(String.format("硬件版本号: %s", "---"));
    }

    private void initContentBt() {
        if (mConnected) {
            connectStatus.setText("断开连接");
        } else {
            connectStatus.setText("重新连接");
        }
    }

    /**
     * 读取硬件版本
     */
    private final BleReadResponse mReadRspHardware = new BleReadResponse() {
        @Override
        public void onResponse(int code, byte[] data) {
            if (code == REQUEST_SUCCESS) {
                KLog.d("硬件版本读取成功!===" + Arrays.toString(data));

                String byteString = SendBltAgreementUtil.byteArrayToHexString(data);
                bleHardwareVersion.setText(String.format("硬件版本号: %s", SendBltAgreementUtil.hexToString(byteString)));
            } else {
                CommonUtils.toastLong("未知原因，硬件版本读取失败");
            }
        }
    };

    /**
     * 读取软件版本
     */
    private final BleReadResponse mReadRspSoftware = new BleReadResponse() {
        @Override
        public void onResponse(int code, byte[] data) {
            if (code == REQUEST_SUCCESS) {
                KLog.d("软件版本读取成功!===" + Arrays.toString(data));

                String byteString = SendBltAgreementUtil.byteArrayToHexString(data);
                bleSoftwareVersion.setText(String.format("软件版本号: %s", SendBltAgreementUtil.hexToString(byteString)));
            } else {
                CommonUtils.toastLong("未知原因，软件版本读取失败");
            }
        }
    };

    private void connectDevice() {
        mTvTitle.setText(String.format("%s%s", getString(R.string.connecting), mDevice.getAddress()));
        mPbar.setVisibility(View.VISIBLE);
        dataContent.setVisibility(View.GONE);

        //配置连接参数
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3) //连接如果失败重试3次
                .setConnectTimeout(20000) //连接超时20s
                .setServiceDiscoverRetry(3) //发现服务如果失败重试3次
                .setServiceDiscoverTimeout(10000) //发现服务超时10s
                .build();

        ClientManager.getClient().connect(mDevice.getAddress(), options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                KLog.e(String.format("profile:\n%s", profile));
                KLog.e("code===" + code);
                mTvTitle.setText(String.format("%s", mDevice.getAddress()));
                mPbar.setVisibility(View.GONE);
                dataContent.setVisibility(View.VISIBLE);

                //连接成功
                if (code == REQUEST_SUCCESS) {
                    setGattProfile(profile);
                }
            }
        });
    }

    public void setGattProfile(BleGattProfile profile) {
        List<DetailItem> items = new ArrayList<DetailItem>();

        List<BleGattService> services = profile.getServices();

        for (BleGattService service : services) {
            items.add(new DetailItem(DetailItem.TYPE_SERVICE, service.getUUID(), null));
            List<BleGattCharacter> characters = service.getCharacters();
            for (BleGattCharacter character : characters) {
                items.add(new DetailItem(DetailItem.TYPE_CHARACTER, character.getUuid(), service.getUUID()));
            }
        }

        parseServiceUuid(items);
    }

    /**
     * 解析服务UUID
     */
    public void parseServiceUuid(List<DetailItem> items) {
        for (DetailItem item : items) {
            if (item.type == DetailItem.TYPE_CHARACTER) {
                KLog.v(String.format("click service = %s, character = %s", item.service, item.uuid));
                //判断服务
                if (item.service.equals(DeviceUuid.BLE_MAIN_SERVICE_UUID)) {
                    //判断是否是蓝牙数据UUID
                    if (item.uuid.equals(DeviceUuid.BLE_WRITE_UUID_UUID)) {
                        //开始通知
                        ClientManager.getClient().notify(mDevice.getAddress(), item.service, item.uuid, mNotifyRsp);
                    }
                } else if (item.service.equals(DeviceUuid.BLE_CHARACTERISTIC_SERVICE_UUID)) {
                    //1、读取硬件版本号
                    if (item.uuid.equals(DeviceUuid.BLE_WRITE_HARDWARE_UUID_UUID)) {
                        ClientManager.getClient().read(mDevice.getAddress(), item.service, item.uuid, mReadRspHardware);
                    }
                    //2、读取软件版本号
                    else if (item.uuid.equals(DeviceUuid.BLE_WRITE_SOFTWARE_UUID_UUID)) {
                        ClientManager.getClient().read(mDevice.getAddress(), item.service, item.uuid, mReadRspSoftware);
                    }
                }
            }
        }
    }

    /**
     * 监听通知温度电量电压
     */
    private final BleNotifyResponse mNotifyRsp = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            if (service.equals(DeviceUuid.BLE_MAIN_SERVICE_UUID) && character.equals(DeviceUuid.BLE_WRITE_UUID_UUID)) {
                //打印数据
                KLog.d(String.format("%s", ByteUtils.byteToString(value)));

                //解析字节数组
                parseByteArray(value);
            }
        }

        @Override
        public void onResponse(int code) {
            if (code == REQUEST_SUCCESS) {
                CommonUtils.toast("开启设备通知成功，获取设备数据");
            } else {
                CommonUtils.toast("未知原因，开启设备通知失败");
            }
        }
    };

    /**
     * 监听信号强度
     */
    private final BleReadRssiResponse mReadRssiResponse = new BleReadRssiResponse() {
        @Override
        public void onResponse(int code, Integer data) {
            if (code == REQUEST_SUCCESS) {
                KLog.d("读取信号强度成功===" + data);
                rssiValue.setText(String.format("信号强度: %sdBm", data));
            } else {
                CommonUtils.toast("未知原因，读取信号强度失败");
            }
        }
    };

    /**
     * 解析字节数组
     *
     * @param data
     */
    public void parseByteArray(byte[] data) {
        if (data.length < 2) {
            KLog.e("数据长度不足==" + Arrays.toString(data));
            return;
        }

        KLog.d("解析数据==" + Arrays.toString(data));

        for (int i = 3; i < data.length - 1; i = i + 2) {
            //每段数据两个字节
            byte[] dataByte = SendBltAgreementUtil.getAppointData(data, i, i + 2);

            float dataValue = (float) SendBltAgreementUtil.switchByteArrayToDecimal(dataByte);

            if (i == 3) {
                KLog.d("电池电压mV===" + dataValue);
                batteryVoltage.setText(String.format("电池电压: %smV", (int) dataValue));
                batteryPercentage.setText(String.format("电池: %s", getBatteryVoltage((int) dataValue)));
            } else {
                if (i == 7) {
                    float ambientTempV = dataValue / 10;
                    ambientTemp.setText(String.format("环境温度: %s°C", ambientTempV));
                    KLog.d("环境温度===" + ambientTempV);
                }
                //temp1
                else if (i == 5) {
                    float temp1V = dataValue / 10;
                    temp1.setText(String.format("温度1: %s°C", temp1V));
                    KLog.d("温度1===" + temp1V);
                }
                //temp2
                else if (i == 9) {
                    float temp2V = dataValue / 10;
                    temp2.setText(String.format("温度2: %s°C", temp2V));
                    KLog.d("温度2===" + dataValue);
                }
                //temp3
                else if (i == 11) {
                    float temp3V = dataValue / 10;
                    temp3.setText(String.format("温度3: %s°C", temp3V));
                    KLog.d("温度3===" + temp3V);
                }
                //temp4
                else if (i == 13) {
                    float temp4V = dataValue / 10;
                    temp4.setText(String.format("温度4: %s°C", temp4V));
                    KLog.d("温度4===" + temp4V);
                }
                //temp5
                else if (i == 15) {
                    float temp5V = dataValue / 10;
                    temp5.setText(String.format("温度5: %s°C", temp5V));
                    KLog.d("温度5===" + temp5V);
                }
                //temp6
                else if (i == 17) {
                    float temp6V = dataValue / 10;
                    temp6.setText(String.format("温度6: %s°C", temp6V));
                    KLog.d("温度6===" + temp6V);
                }
            }
        }

        //信号值
        ClientManager.getClient().readRssi(mDevice.getAddress(), mReadRssiResponse);
    }

    /**
     * 电量算法
     * 0-2000mv 0%
     * 2000-2300mv 0-50%
     * 2300-2500mv 50-100%
     * 2500mv以上100%
     */
    private String getBatteryVoltage(int voltage) {
        if (voltage <= 2000) {
            return "0%";
        } else if (voltage <= 2300) {
            float v = (float) (voltage - 2000) / (2300 - 2000) * 0.5f;
            return String.format(Locale.US, "%.1f%%", v * 100);
        } else if (voltage <= 2500) {
            float v = (float) (voltage - 2300) / (2500 - 2300) * 0.5f + 0.5f;
            return String.format(Locale.US, "%.1f%%", v * 100);
        } else {
            return "100%";
        }
    }

    /**
     * 监听蓝牙连接状态
     */
    private final BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            KLog.d(String.format(Locale.US, "监听蓝牙连接状态 DeviceDetailActivity onConnectStatusChanged %d in %s",
                    status, Thread.currentThread().getName()));

            //连接状态
            mConnected = (status == STATUS_CONNECTED);

            //连接成功后执行
            if (status == STATUS_CONNECTED) {
                connectDeviceIfNeeded();
                initContentBt();
            }
            //连接失败返回
            else if (status == STATUS_DISCONNECTED) {
                //判断是否是主动断开
                if (!isClickDisconnect) {
                    CommonUtils.toastLong("连接失败，请检查设备是否已被连接，或停止广播!");

                    initContentBt();
//                    mTvTitle.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            finish();
//                        }
//                    }, 300);
                }
            }
        }
    };

    private void connectDeviceIfNeeded() {
        if (!mConnected) {
            connectDevice();
        }
    }

    @Override
    protected void onDestroy() {
        //断开连接
        ClientManager.getClient().disconnect(mDevice.getAddress());
        //取消注册
        ClientManager.getClient().unregisterConnectStatusListener(mDevice.getAddress(), mConnectStatusListener);
        super.onDestroy();
    }
}
