package com.me.ml;

/**
 * @author ml
 * @date 2024/9/10 16:56
 */
public interface OnPopupCallback {
    //输入蓝牙搜索设备名回调
    void onInputDeviceName(String deviceName);

    //搜索信号范围回调
    void onSearchRange(int range);

    //是否显示已收藏
    void onShowFavorite(boolean show);

    //是否持续搜索
    void onContinueSearch(boolean continueSearch);

    //隐藏空名称蓝牙
    void onHideEmptyName(boolean hideEmptyName);
}
