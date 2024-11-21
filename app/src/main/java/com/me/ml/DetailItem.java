package com.me.ml;

import java.util.UUID;

/**
 * @author ml
 * @date 2024/9/7 10:28
 */
public class DetailItem {

    public static final int TYPE_SERVICE = 0;
    public static final int TYPE_CHARACTER = 1;

    public int type;

    public UUID uuid;

    public UUID service;

    public DetailItem(int type, UUID uuid, UUID service) {
        this.type = type;
        this.uuid = uuid;
        this.service = service;
    }
}
