package com.me.ml.bluetooth_kit.utils.hook.compat;

import android.os.IBinder;

import com.me.ml.bluetooth_kit.utils.hook.utils.HookUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ServiceManagerCompat {

    private static Class<?> serviceManager;
    private static Field sCache;
    private static Method getService;

    static {
        serviceManager = HookUtils.getClass("android.os.ServiceManager");

        sCache = HookUtils.getField(serviceManager, "sCache");
        sCache.setAccessible(true);

        getService = HookUtils.getMethod(serviceManager, "getService", String.class);
    }

    public static Class<?> getServiceManager() {
        return serviceManager;
    }

    public static Field getCacheField() {
        return sCache;
    }

    public static HashMap<String, IBinder> getCacheValue() {
        return HookUtils.getValue(sCache);
    }

    public static Method getService() {
        return getService;
    }
}
