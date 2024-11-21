package com.me.ml.xpopup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.me.ml.OnPopupCallback;
import com.oyml.bluetooth.R;
import com.me.ml.repository.DeviceSearchRepository;
import com.lxj.xpopup.impl.PartShadowPopupView;
import com.me.ml.utils.log.KLog;

import java.util.Locale;

/**
 * 自定义条件筛选弹窗
 *
 * @author ml
 * @date 2024/09/10
 */
public class CustomPartShadowPopupView extends PartShadowPopupView {
    private OnPopupCallback popupCallback;
    private EditText editText;
    private SeekBar seekBar;
    private TextView seekBarValue;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch onlyDisplay;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch continuousSearch;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch hideEmptyName;

    public CustomPartShadowPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_part_shadow_popup;
    }

    TextView text;

    @Override
    protected void onCreate() {
        super.onCreate();
        text = findViewById(R.id.text);

        //蓝牙设备名
        editText = findViewById(R.id.edit_ble_name);

        //滑动条
        seekBar = findViewById(R.id.seekBar);
        //滑动条显示的值
        seekBarValue = findViewById(R.id.seekBarValue);

        //仅显示已收藏
        onlyDisplay = findViewById(R.id.only_display_bookmarked_items);
        //持续搜索
        continuousSearch = findViewById(R.id.continuous_search);
        //隐藏空名称蓝牙
        hideEmptyName = findViewById(R.id.hide_empty_name_ble);

        applyDefaultValue();

        //蓝牙设备名事件监听
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                KLog.d("监听输入的蓝牙设备名==", s.toString());

                if (popupCallback != null) {
                    popupCallback.onInputDeviceName(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //滑动条事件监听
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //更新 TextView 显示当前值
                seekBarValue.setText(String.format(Locale.US, "-%d dBm", progress));

                if (popupCallback != null) {
                    popupCallback.onSearchRange(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //用户开始拖动滑动条
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //用户停止拖动滑动条
            }
        });

        //仅显示已收藏开关监听
        onlyDisplay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (popupCallback != null) {
                    popupCallback.onShowFavorite(isChecked);
                }
            }
        });

        //是否持续搜索
        continuousSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (popupCallback != null) {
                    popupCallback.onContinueSearch(isChecked);
                }
            }
        });

        //隐藏空名称蓝牙
        hideEmptyName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (popupCallback != null) {
                    popupCallback.onHideEmptyName(isChecked);
                }
            }
        });
    }

    /**
     * 设置默认值
     */
    private void applyDefaultValue() {
        String mDeviceName = DeviceSearchRepository.getInstance().getDeviceName();
        int mSearchRange = DeviceSearchRepository.getInstance().getRssiRange();
        boolean isOnlyDisplay = DeviceSearchRepository.getInstance().getIsOnlyFavorite();
        boolean isContinuousSearch = DeviceSearchRepository.getInstance().getIsContinueSearch();
        boolean isHideEmptyName = DeviceSearchRepository.getInstance().getIsHideEmptyName();

        editText.setText(mDeviceName);
        seekBar.setProgress(mSearchRange);
        seekBarValue.setText(String.format(Locale.US, "-%d dBm", mSearchRange));
        onlyDisplay.setChecked(isOnlyDisplay);
        continuousSearch.setChecked(isContinuousSearch);
        hideEmptyName.setChecked(isHideEmptyName);
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    //设置回调接口的方法
    public void setPopupCallback(OnPopupCallback callback) {
        this.popupCallback = callback;
    }
}
