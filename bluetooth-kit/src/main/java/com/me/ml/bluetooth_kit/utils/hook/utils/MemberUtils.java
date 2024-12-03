package com.me.ml.bluetooth_kit.utils.hook.utils;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

public class MemberUtils {
    static boolean isAccessible(final Member m) {
        return m != null && Modifier.isPublic(m.getModifiers()) && !m.isSynthetic();
    }
}
