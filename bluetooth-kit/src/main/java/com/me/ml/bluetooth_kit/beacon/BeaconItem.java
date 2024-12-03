package com.me.ml.bluetooth_kit.beacon;

import com.me.ml.bluetooth_kit.utils.ByteUtils;

public class BeaconItem {

    /**
     * 广播中声明的长度
     */
    public int len;

    /**
     * 广播中声明的type
     */
    public int type;

    /**
     * 广播中的数据部分
     */
    public byte[] bytes;

    /**
     * 重写 toString 方法，返回 BeaconItem 对象的字符串表示
     *
     * @return 字符串表示，包含长度、类型和数据部分
     */
    @Override
    public String toString() {
        // 初始化字符串格式化模板，用于格式化字节数据
        String format = "";

        // 创建一个可变字符串构建器
        StringBuilder sb = new StringBuilder();

        // 格式化并添加长度和类型信息到字符串构建器
        sb.append(String.format("@Len = %02X, @Type = 0x%02X", len, type));

        // 根据类型设置格式化模板
        switch (type) {
            case 8:
            case 9:
                // 对于类型 8 和 9，使用字符格式化
                format = "%c";
                break;
            default:
                // 对于其他类型，使用十六进制格式化
                format = "%02X ";
                break;
        }

        // 添加箭头符号表示数据部分
        sb.append(" -> ");

        // 创建一个新的可变字符串构建器来处理数据部分
        StringBuilder sbSub = new StringBuilder();
        try {
            // 遍历数据字节数组，将每个字节格式化为字符串并添加到 sbSub
            for (byte b : bytes) {
                sbSub.append(String.format(format, b & 0xff));
            }
            // 将 sbSub 的内容添加到主字符串构建器 sb 中
            sb.append(sbSub.toString());
        } catch (Exception e) {
            // 如果发生异常，使用默认的字节到字符串转换方法
            sb.append(ByteUtils.byteToString(bytes));
        }

        // 返回最终构建的字符串
        return sb.toString();
    }
}
