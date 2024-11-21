package com.me.ml.ble;

import java.util.Arrays;

/**
 * @author ml
 * @date 2024/9/11 10:37
 */
public class SendBltAgreementUtil {


    /**
     * 截取指定长度的数据
     *
     * @param bytes
     * @param startLength
     * @param endLength
     * @return
     */
    public static byte[] getAppointData(byte[] bytes, int startLength, int endLength) {
        return Arrays.copyOfRange(bytes, startLength, endLength);
    }

    /**
     * 将字节数组转换为16进制字符串
     *
     * @param data
     * @return
     */
    public static String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    /**
     * 将16进制的字符串转成对应字符
     *
     * @param hex 16进制的字符串
     */
    public static String hexToString(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String hexValue = hex.substring(i, i + 2);
            int decimalValue = Integer.parseInt(hexValue, 16);
            stringBuilder.append((char) decimalValue);
        }

        return stringBuilder.toString();
    }

    /**
     * 将字节数组转成十进制数字
     *
     * @param bytes
     * @return
     */
    public static int switchByteArrayToDecimal(byte[] bytes) {
        if (bytes.length == 2) {
            return byteArrayToDecimalShort(bytes);
        } else {
            return byteArrayToDecimalInt(bytes);
        }
    }

    /**
     * 字节数组转成十进制数字
     * 16位
     *
     * @param bytes 字节数组
     * @return 十进制数字
     */
    public static int byteArrayToDecimalShort(byte[] bytes) {
        short timestamp = 0;
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[bytes.length - 1 - i];

            timestamp = (short) ((timestamp << 8) | (b & 0xFF));
        }

        return timestamp;
    }

    /**
     * 字节数组转成十进制数字
     * 32位
     *
     * @param bytes 字节数组
     * @return 十进制数字
     */
    public static int byteArrayToDecimalInt(byte[] bytes) {
        int timestamp = 0;
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[bytes.length - 1 - i];

            timestamp = ((timestamp << 8) | (b & 0xFF));
        }

        return timestamp;
    }
}
