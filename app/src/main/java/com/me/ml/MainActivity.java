package com.me.ml;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.me.ml.service.BbqovnService;
import com.me.ml.utils.CommonUtils;
import com.me.ml.utils.bar.StatusBarUtil;
import com.me.ml.constant.AppConstants;
import com.me.ml.bluetooth_kit.connect.listener.BluetoothStateListener;
import com.me.ml.bluetooth_kit.search.SearchRequest;
import com.me.ml.bluetooth_kit.search.SearchResult;
import com.me.ml.bluetooth_kit.search.response.SearchResponse;
import com.me.ml.repository.DeviceSearchRepository;
import com.me.ml.utils.BTUtils;
import com.me.ml.utils.VersionUtils;
import com.me.ml.view.PullRefreshListView;
import com.me.ml.view.PullToRefreshFrameLayout;
import com.me.ml.xpopup.CustomPartShadowPopupView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.me.ml.utils.log.KLog;
import com.oyml.bluetooth.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author ml
 * @date 2024/9/7 10:28
 */
public class MainActivity extends Activity {
    private PullToRefreshFrameLayout mRefreshLayout;
    private PullRefreshListView mListView;
    private DeviceListAdapter mAdapter;
    private TextView mTvTitle;
    private TextView mAppVersion;
    private TextView mFilterCriteria;
    private CustomPartShadowPopupView popupView;
    private List<SearchResult> mDeviceList;
    private HashMap<String, SearchResult> mDeviceMap;
    //搜索设备名
    private String mDeviceName;
    //搜索信号范围
    private int mSearchRange = -100;
    //是否隐藏空名称蓝牙，默认隐藏
    private boolean isHideEmptyName = true;
    //是否持续搜索
    private boolean isContinuousSearch;
    //是否只显示收藏
    private boolean isOnlyDisplay;
    //单次搜索次数，默认1次
    private static final int SINGLE_SEARCH_COUNT = 1;
    /**
     * 单次搜索时间长度，单位ms，默认7.6s
     * BLE 扫描频率限制》》》
     * 在 Android 8.1 及以上系统中，如果你多次启动扫描操作，系统会对频繁扫描进行限制，避免过多消耗资源。具体的限制规则：
     * 1、每个应用 只能在 30 秒内发起 4 次 扫描请求。
     * 2、如果超过这个频率，系统将抛出 ScanTooFrequentlyException 错误，并阻止进一步的扫描操作。
     */
    private static final int SINGLE_SEARCH_TIME = 7600;
    //搜索状态标志---避免搜索中重复搜索
    private boolean isSearching = false;
    private Handler mHandler = new Handler();
    private Runnable mUpdateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置状态栏，兼容4.4
        StatusBarUtil.setColor(this, CommonUtils.getColor(this, R.color.colorToolBar), 0);

        mDeviceMap = new HashMap<>();

        mTvTitle = (TextView) findViewById(R.id.title);
        mAppVersion = (TextView) findViewById(R.id.app_version);
        mFilterCriteria = (TextView) findViewById(R.id.filter_criteria);
        mRefreshLayout = (PullToRefreshFrameLayout) findViewById(R.id.pulllayout);

        mListView = mRefreshLayout.getPullToRefreshListView();
        mAdapter = new DeviceListAdapter(this);
        mListView.setAdapter(mAdapter);

        applyDefaultValue();

        //下拉刷新
        mListView.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {

                KLog.i("蓝牙是否打开：" + BTUtils.isDisabled());
                //判断蓝牙是否打开
                if (BTUtils.isDisabled()) {
                    //每次下拉后清空设备列表
                    if (mDeviceList != null) {
                        mDeviceMap.clear();
                        mDeviceList.clear();
                        mDeviceList.addAll(mDeviceMap.values());
                        mAdapter.setDataList(mDeviceList);
                    }

                    CommonUtils.toastLong("请先打开手机蓝牙开关");
                    applyBluetooth();

                    mListView.onRefreshComplete(true);

                    mRefreshLayout.showState(AppConstants.ALLOW_PULL_IN_EMPTY_PAGE);
                } else {
                    applyBluetoothPermission();
                }
            }
        });

        searchDevice();

        //注册蓝牙开关状态监听器
        ClientManager.getClient().registerBluetoothStateListener(mBluetoothStateListener);

        filterCriteria();

        //创建一个 Runnable，用于更新设备列表
        mUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                refreshList();

                if (mDeviceMap.size() > 0) {
                    mRefreshLayout.showState(AppConstants.LIST);
                }
                mHandler.postDelayed(this, 650);
            }
        };

        mAppVersion.setText(String.format("版本：%s", VersionUtils.getVersionName(this)));
    }

    /**
     * 刷新列表
     */
    private void refreshList() {
        if (mDeviceList != null && mDeviceMap != null && mAdapter != null) {
            mDeviceList.clear();
            mDeviceList.addAll(mDeviceMap.values());
            mAdapter.setDataList(mDeviceList);
        } else {
            KLog.e("刷新列表失败");
        }
    }

    /**
     * 蓝牙开关状态监听器回调
     */
    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            if (openOrClosed) {
                searchDevice();
                KLog.d("蓝牙已打开");
            } else {
                KLog.d("蓝牙已关闭");
            }
        }
    };

    /**
     * 设置默认值
     */
    private void applyDefaultValue() {
        mDeviceName = DeviceSearchRepository.getInstance().getDeviceName();
        mSearchRange = DeviceSearchRepository.getInstance().getRssiRange();
        isOnlyDisplay = DeviceSearchRepository.getInstance().getIsOnlyFavorite();
        isContinuousSearch = DeviceSearchRepository.getInstance().getIsContinueSearch();
        isHideEmptyName = DeviceSearchRepository.getInstance().getIsHideEmptyName();

        mSearchRange = -mSearchRange;
        if (mDeviceName == null) {
            mFilterCriteria.setText("点击设置搜索名称");
        } else {
            mFilterCriteria.setText(mDeviceName);
        }
    }

    /**
     * 筛选条件弹窗
     */
    private void filterCriteria() {
        mFilterCriteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPartShadow(v);
            }
        });
    }

    private void showPartShadow(final View v) {
        if (popupView == null) {
            //创建 CustomPartShadowPopupView 实例
            CustomPartShadowPopupView customPopupView = new CustomPartShadowPopupView(this);

            //设置自定义回调
            customPopupView.setPopupCallback(new OnPopupCallback() {
                @Override
                public void onInputDeviceName(String deviceName) {
                    KLog.d("输入蓝牙搜索设备名==" + deviceName);
                    if (TextUtils.isEmpty(deviceName)) {
                        mFilterCriteria.setText("点击设置搜索名称");
                    } else {
                        mFilterCriteria.setText(deviceName);
                    }
                    mDeviceName = deviceName;

                    //保存搜索设备名
                    DeviceSearchRepository.getInstance().saveDeviceName(deviceName);
                }

                @Override
                public void onSearchRange(int range) {
                    KLog.d("搜索信号范围==" + -range);
                    mSearchRange = -range;

                    //保存搜索信号范围
                    DeviceSearchRepository.getInstance().saveRssiRange(range);
                }

                @Override
                public void onShowFavorite(boolean show) {
                    KLog.d("是否显示已收藏==" + show);
                    isOnlyDisplay = show;

                    //保存是否显示已收藏
                    DeviceSearchRepository.getInstance().saveIsOnlyFavorite(show);
                }

                @Override
                public void onContinueSearch(boolean continueSearch) {
                    KLog.d("是否持续搜索==" + continueSearch);
                    isContinuousSearch = continueSearch;

                    //保存是否持续搜索
                    DeviceSearchRepository.getInstance().saveIsContinueSearch(continueSearch);
                }

                @Override
                public void onHideEmptyName(boolean hideEmptyName) {
                    KLog.d("隐藏空名称蓝牙==" + hideEmptyName);
                    isHideEmptyName = hideEmptyName;

                    //保存是否隐藏空名称
                    DeviceSearchRepository.getInstance().saveIsHideEmptyName(hideEmptyName);
                }
            });

            popupView = (CustomPartShadowPopupView) new XPopup.Builder(this)
                    .atView(v)
                    .autoOpenSoftInput(true)
                    .setPopupCallback(new SimpleCallback() {
                        @Override
                        public void onShow(BasePopupView popupView) {
                            KLog.d("显示了");
                        }

                        @Override
                        public void onDismiss(BasePopupView popupView) {
                        }
                    })
                    .asCustom(customPopupView);
        }

        popupView.show();
    }

    private void applyBluetoothPermission() {
        KLog.d("申请蓝牙权限");
        final Context ctx = this;
        XXPermissions.with(ctx)
                .permission(Permission.Group.BLUETOOTH)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean allGranted) {

                        //每次下拉后清空设备列表
                        mDeviceMap.clear();

                        //蓝牙搜索中，下拉后立马关闭下拉
                        if (isSearching) {
                            //200ms延迟关闭下拉
                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //关闭下拉
                                    mListView.onRefreshComplete(true);
                                }
                            }, 200);
                            return;
                        }

                        searchDevice();
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                    }
                });
    }

    /**
     * 申请手机蓝牙开关
     */
    private void applyBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // >=12.0
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                KLog.d("手机蓝牙状态已开--12及以上： 已打开蓝牙弹窗");
                return;
            }

            KLog.d("手机蓝牙状态已开--12及以上： 没有打开蓝牙弹窗");
            startActivity(enableBtIntent);
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                KLog.d("手机蓝牙状态已开--12以下： 已打开蓝牙弹窗");
                return;
            }
            KLog.d("手机蓝牙状态已开--12以下： 没有打开蓝牙弹窗");

            startActivity(enableBtIntent);
        }
    }

    /**
     * 控制搜索方式
     */
    private void searchDevice() {
        //如果已经在搜索中，则直接返回
        if (isSearching) {
            KLog.w("搜索已经进行中，停止重复调用搜索");
            return;
        }

        //标记为正在搜索
        isSearching = true;

        stopSearch();

        //判断是否是持续搜索
        if (isContinuousSearch) {
            performContinuousSearch();
        } else {
            //否则进行默认的搜索
            performSearch();
        }
    }

    /**
     * 搜索请求
     */
    private final SearchRequest request = new SearchRequest.Builder()
            .searchBluetoothLeDevice(SINGLE_SEARCH_TIME, SINGLE_SEARCH_COUNT) //先扫BLE设备xx次，每次xx秒
            .build();

    /**
     * 持续搜索
     */
    private final SearchRequest continuousSearchRequest = new SearchRequest.Builder()
            .searchBluetoothLeDevice(0, SINGLE_SEARCH_COUNT)
            .build();

    /**
     * 单次搜索设备
     */
    private void performSearch() {
        ClientManager.getClient().search(request, mSearchResponse);
    }

    /**
     * 持续搜索设备
     */
    private void performContinuousSearch() {
        ClientManager.getClient().search(continuousSearchRequest, mSearchResponse);
    }

    /**
     * 停止搜索
     */
    private void stopSearch() {
        //停止搜索
        ClientManager.getClient().stopSearch();
    }

    private final SearchResponse mSearchResponse = new SearchResponse() {

        /**
         * 搜索开始
         */
        @Override
        public void onSearchStarted() {
            KLog.d("onSearchStarted---搜索开始");
            //开始搜索前清空列表
            mDeviceMap.clear();
            mListView.onRefreshComplete(true);
            mRefreshLayout.showState(AppConstants.LIST);
            mTvTitle.setText(R.string.string_refreshing);
        }

        /**
         * 发现设备
         */
        @Override
        public void onDeviceFounded(SearchResult device) {
            KLog.d("发现的设备地址==" + device.device.getAddress() + " 信号范围==" + device.rssi + " 蓝牙名称==" + device.device.getName());

            //是否显示已收藏
            if (isOnlyDisplay) {
                if (!DeviceSearchRepository.getInstance().getFavoriteList().contains(device.device.getAddress())) {
                    return;
                }
            }

            //蓝牙名称
            if (mDeviceName == null || mDeviceName.length() == 0) {
                //处理 mDeviceName 为空的情况
            } else {
                // 设备的名称获取并处理
                String deviceName = device.device.getName();

                //不区分大小写，并且设备名称以 mDeviceName 开头
                if (deviceName == null || !deviceName.toLowerCase().startsWith(mDeviceName.toLowerCase())) {
                    return;
                }
            }

            //搜索信号范围
            if (mSearchRange != -100) {
                if (device.rssi < mSearchRange || device.rssi == 0) {
                    return;
                }
            }

            //隐藏空名称
            if (isHideEmptyName && TextUtils.isEmpty(device.device.getName())) {
                return;
            }

            //添加到集合
            mDeviceMap.put(device.device.getAddress(), device);

            if (mDeviceList == null) {
                mDeviceList = new ArrayList<>();
            } else {
                //清空列表以复用
                mDeviceList.clear();
            }

            //启动更新逻辑
            if (!mHandler.hasMessages(0)) { //确保没有在运行中
                mHandler.post(mUpdateRunnable);
            }
        }

        /**
         * 搜索结束
         */
        @Override
        public void onSearchStopped() {
            KLog.d("onSearchStopped---搜索结束");
            mListView.onRefreshComplete(true);
            mRefreshLayout.showState(AppConstants.LIST);
            mTvTitle.setText(R.string.devices);

            //停止更新
            mHandler.removeCallbacks(mUpdateRunnable);
            //重置搜索状态
            isSearching = false;
        }

        /**
         * 搜索取消
         */
        @Override
        public void onSearchCanceled() {
            KLog.d("onSearchCanceled---搜索取消");
            mListView.onRefreshComplete(true);
            mRefreshLayout.showState(AppConstants.LIST);
            mTvTitle.setText(R.string.devices);

            //停止更新
            mHandler.removeCallbacks(mUpdateRunnable);
            //重置搜索状态
            isSearching = false;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        stopSearch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册
        ClientManager.getClient().unregisterBluetoothStateListener(mBluetoothStateListener);

        //停止前台服务
        Intent serviceIntent = new Intent(this, BbqovnService.class);
        stopService(serviceIntent);
    }
}
