package com.me.ml.ble;

import java.util.UUID;

/**
 * @author ml
 * @date 2024/9/10 20:59
 */
public class DeviceUuid {
    /**
     * 主服务 UUID
     */
    private static final String SERVICE_UUID = "8800c4fb-a175-b666-2ebb-1e97e6fa47b3";

    /**
     * 特征服务 UUID
     */
    private static final String CHARACTERISTIC_SERVICE_UUID = "0000180a-0000-1000-8000-00805f9b34fb";

    /**
     * 特性读取 UUID
     */
    private static final String CHARACTERISTIC_READ_UUID = "4c0a0ff9-1c6d-1c49-e07f-10f1c55905ca";

    /**
     * 设备硬件描述读取 UUID
     */
    private static final String BLE_WRITE_HARDWARE_UUID = "00002a27-0000-1000-8000-00805f9b34fb";

    /**
     * 设备软件描述读取 UUID
     */
    private static final String BLE_WRITE_SOFTWARE_UUID = "00002a28-0000-1000-8000-00805f9b34fb";

    /**
     * 蓝牙主服务UUID---8800c4fb-a175-b666-2ebb-1e97e6fa47b3
     */
    public static final UUID BLE_MAIN_SERVICE_UUID = UUID.fromString(SERVICE_UUID);

    /**
     * 特征服务UUID---0000180a-0000-1000-8000-00805f9b34fb
     */
    public static final UUID BLE_CHARACTERISTIC_SERVICE_UUID = UUID.fromString(CHARACTERISTIC_SERVICE_UUID);

    /**
     * 特性读取UUID---4c0a0ff9-1c6d-1c49-e07f-10f1c55905ca
     */
    public static final UUID BLE_WRITE_UUID_UUID = UUID.fromString(CHARACTERISTIC_READ_UUID);

    /**
     * 设备软件描述读取UUID---00002a28-0000-1000-8000-00805f9b34fb
     */
    public static final UUID BLE_WRITE_SOFTWARE_UUID_UUID = UUID.fromString(BLE_WRITE_SOFTWARE_UUID);

    /**
     * 设备硬件描述读取UUID---00002a27-0000-1000-8000-00805f9b34fb
     */
    public static final UUID BLE_WRITE_HARDWARE_UUID_UUID = UUID.fromString(BLE_WRITE_HARDWARE_UUID);
}
