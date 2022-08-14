package com.jingos.security;

import android.os.Build;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Constants {

    public static final String[] DEVICES_INFO = {"jingos", "jingpad", "jingphone"};

    /**
     * the validate fields
     *
     * @param source
     * @return
     */
    public static boolean validate(String source) {
        for (String device : DEVICES_INFO) {
            if (source.contains(device)) {
                return true;
            }
        }
        return false;
    }

    public static String getDeviceInfo(boolean isValidateModel) {
        String device = getDevice();
        String product = getProduct();
        String deviceBrand = getDeviceBrand();
        String systemModel = getSystemModel();
        String systemVersion = getSystemVersion();
        StringBuilder stringBuilder = new StringBuilder();
        if (isValidateModel) {
            stringBuilder.append(systemModel);
        }
        if (!TextUtils.isEmpty(device)) {
            stringBuilder.append("$").append(device);
        }
        if (!TextUtils.isEmpty(product)) {
            stringBuilder.append("$").append(product);
        }
        if (!TextUtils.isEmpty(systemVersion)) {
            stringBuilder.append("$").append(systemVersion);
        }
        String devicesInfo = stringBuilder.append(deviceBrand).toString().toLowerCase();
        return devicesInfo;
    }

    /**
     * get UUID by devices info
     *
     * @return UUID
     */
    public static String getDeviceUUID() {
        try {
            String dev = "3883756" +
                    Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +
                    Build.DEVICE.length() % 10 +
                    Build.HARDWARE.length() % 10 +
                    Build.ID.length() % 10 +
                    Build.MODEL.length() % 10 +
                    Build.PRODUCT.length() % 10 +
                    Build.SERIAL.length() % 10;
            return new UUID(dev.hashCode(),
                    Build.SERIAL.hashCode()).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }


    /**
     * get system version with release
     *
     * @return system
     */
    public static String getSystemVersion() {
        return android.os.Build.DISPLAY;
    }

    /**
     * get brand name
     *
     * @return brand
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * get product
     *
     * @return brand
     */
    public static String getProduct() {
        return Build.PRODUCT;
    }

    /**
     * get device
     *
     * @return brand
     */
    public static String getDevice() {
        return Build.DEVICE;
    }

    /**
     * get device brand
     *
     * @return brand
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * the system version
     *
     * @return brand
     */
    public static String getSystemCode() {
        return Build.VERSION.CODENAME;
    }

    public static String encryptMd5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
