package com.me.ml.bluetooth_kit.beacon;

import com.me.ml.bluetooth_kit.utils.ByteUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析 BLE 广播数据
 */
public class BeaconParser {

    private byte[] bytes;

    private ByteBuffer mByteBuffer;

    public BeaconParser(BeaconItem item) {
        this(item.bytes);
    }

    public BeaconParser(byte[] bytes) {
        this.bytes = bytes;
        mByteBuffer = ByteBuffer.wrap(bytes).order(
                ByteOrder.LITTLE_ENDIAN);
    }

    public void setPosition(int position) {
        mByteBuffer.position(position);
    }

    public int readByte() {
        return mByteBuffer.get() & 0xff;
    }

    public int readShort() {
        return mByteBuffer.getShort() & 0xffff;
    }

    public boolean getBit(int n, int index) {
        return (n & (1 << index)) != 0;
    }

    /**
     * 解析蓝牙信标广播数据，返回一个 BeaconItem 对象列表
     *
     * @param bytes 蓝牙信标广播数据的字节数组
     * @return 解析后得到的 BeaconItem 对象列表
     */
    public static List<BeaconItem> parseBeacon(byte[] bytes) {
        // 创建一个 ArrayList 来存储解析后的 BeaconItem 对象
        ArrayList<BeaconItem> items = new ArrayList<BeaconItem>();

        // 遍历字节数组，解析每个 BeaconItem 对象
        for (int i = 0; i < bytes.length; ) {
            // 调用 parse 方法解析从索引 i 开始的字节数组，得到一个 BeaconItem 对象
            BeaconItem item = parse(bytes, i);
            // 如果解析成功（item 不为 null），则将其添加到 items 列表中
            if (item != null) {
                items.add(item);
                // 更新索引 i，跳过已经解析的 BeaconItem 对象的长度和类型字节
                i += item.len + 1;
            } else {
                // 如果解析失败（item 为 null），则停止解析
                break;
            }
        }

        // 返回解析后的 BeaconItem 对象列表
        return items;
    }

    /**
     * 从字节数组中解析出一个 BeaconItem 对象
     *
     * @param bytes      字节数组
     * @param startIndex 开始解析的索引位置
     * @return 解析后的 BeaconItem 对象，如果解析失败则返回 null
     */
    private static BeaconItem parse(byte[] bytes, int startIndex) {
        BeaconItem item = null;

        if (bytes.length - startIndex >= 2) {
            byte length = bytes[startIndex];
            if (length > 0) {
                byte type = bytes[startIndex + 1];
                int firstIndex = startIndex + 2;

                if (firstIndex < bytes.length) {
                    item = new BeaconItem();

                    int endIndex = firstIndex + length - 2;

                    if (endIndex >= bytes.length) {
                        endIndex = bytes.length - 1;
                    }

                    item.type = type & 0xff;
                    item.len = length;

                    item.bytes = ByteUtils.getBytes(bytes, firstIndex, endIndex);
                }
            }
        }

        return item;
    }
}
