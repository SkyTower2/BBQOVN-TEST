package com.me.ml.repository;

import com.me.ml.utils.log.KLog;
import com.me.ml.utils.constant.MMKVConstant;
import com.me.ml.utils.mmkv.MMKVUtils;
import com.me.ml.utils.constant.SPConstant;
import com.me.ml.utils.sp.SPUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ml
 * @date 2024/9/10 17:32
 */
public class DeviceSearchRepository {
    //创建一个 Set<String> 集合，用于存储已收藏的蓝牙设备地址
    private Set<String> stringSet = new HashSet<>();
    //默认值，如果未找到已收藏列表则返回这个
    Set<String> defaultSet = new HashSet<>();
    private static DeviceSearchRepository instance;

    public static DeviceSearchRepository getInstance() {
        if (instance == null) {
            synchronized (DeviceSearchRepository.class) {
                if (instance == null) {
                    instance = new DeviceSearchRepository();
                }
            }
        }
        return instance;
    }

    /**
     * 保存蓝牙设备名称到SP
     */
    public void saveDeviceName(String deviceName) {
        if (deviceName.isEmpty()) {
            deviceName = null;
        }
        MMKVUtils.putString(MMKVConstant.DEVICE_NAME, deviceName);
    }

    /**
     * 获取蓝牙设备名称
     *
     * @return 默认null
     */
    public String getDeviceName() {
        return MMKVUtils.getString(MMKVConstant.DEVICE_NAME, null);
    }

    /**
     * 保存搜索信号范围
     */
    public void saveRssiRange(int rssiRange) {
        SPUtils.getInstance().put(SPConstant.RSSI_RANGE, rssiRange);
    }

    /**
     * 获取搜索信号范围
     *
     * @return 默认100
     */
    public int getRssiRange() {
        return SPUtils.getInstance().getInt(SPConstant.RSSI_RANGE, 100);
    }

    /**
     * 保存是否显示已收藏开关
     */
    public void saveIsOnlyFavorite(boolean isOnlyFavorite) {
        SPUtils.getInstance().put(SPConstant.IS_ONLY_FAVORITE, isOnlyFavorite);
    }

    /**
     * 获取是否显示已收藏开关
     *
     * @return 默认false关闭
     */
    public boolean getIsOnlyFavorite() {
        return SPUtils.getInstance().getBoolean(SPConstant.IS_ONLY_FAVORITE, false);
    }

    /**
     * 保存持续搜索
     */
    public void saveIsContinueSearch(boolean isContinueSearch) {
        SPUtils.getInstance().put(SPConstant.IS_CONTINUE_SEARCH, isContinueSearch);
    }

    /**
     * 获取持续搜索
     *
     * @return 默认false关闭
     */
    public boolean getIsContinueSearch() {
        return SPUtils.getInstance().getBoolean(SPConstant.IS_CONTINUE_SEARCH, false);
    }

    /**
     * 保存是否隐藏蓝牙空名称
     */
    public void saveIsHideEmptyName(boolean isHideEmptyName) {
        SPUtils.getInstance().put(SPConstant.IS_HIDE_EMPTY_NAME, isHideEmptyName);
    }

    /**
     * 获取是否隐藏蓝牙空名称
     *
     * @return 默认true开启
     */
    public boolean getIsHideEmptyName() {
        return SPUtils.getInstance().getBoolean(SPConstant.IS_HIDE_EMPTY_NAME, true);
    }

    /**
     * 获取收藏列表
     */
    public Set<String> getFavoriteList() {
        return SPUtils.getInstance().getStringSet(SPConstant.FAVORITE_LIST_KEY, defaultSet);
    }

    /**
     * 添加蓝牙设备到收藏列表
     */
    public void addDeviceToFavorite(String deviceAddress) {
        KLog.d("添加蓝牙设备到收藏列表==" + deviceAddress);
        stringSet.add(deviceAddress);
        SPUtils.getInstance().put(SPConstant.FAVORITE_LIST_KEY, stringSet);
    }

    /**
     * 从收藏列表移除蓝牙设备
     */
    public void removeDeviceFromFavorite(String deviceAddress) {
        KLog.d("从收藏列表移除蓝牙设备==" + deviceAddress);
        stringSet.remove(deviceAddress);
        SPUtils.getInstance().put(SPConstant.FAVORITE_LIST_KEY, stringSet);
    }
}
