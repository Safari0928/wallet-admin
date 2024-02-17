package com.mdd.mobile;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class LikeMobileThreadLocal {

    /**
     * 构造方法
     */
    public LikeMobileThreadLocal() {}

    /**
     * 取得本地线程对象
     */
    private static final java.lang.ThreadLocal<Map<String, Object>> MY_LOCAL = new java.lang.ThreadLocal<>();

    /**
     * 写入本地线程
     */
    public static void put(String key, Object val) {
        Map<String, Object> map = MY_LOCAL.get();
        if (map == null) {
            synchronized (MY_LOCAL) {
                map = new ConcurrentSkipListMap<>();
            }
        }
        map.put(key, val);
        MY_LOCAL.set(map);
    }

    /**
     * 获取本地线程
     */
    public static Object get(String key) {
        Map<String, Object> map = MY_LOCAL.get();
        if (map == null) {
            return null;
        }
        return map.getOrDefault(key, "");
    }

    /**
     * 获取用户ID
     */
    public static String  getUserId() {
        Object adminId = LikeMobileThreadLocal.get("userId");
        if (adminId == null || adminId.toString().equals("")) {
            return null;
        }
        return adminId.toString();
    }

    public static String  getPayId() {
        Object payId = LikeMobileThreadLocal.get("payId");
        if (payId == null || payId.toString().equals("")) {
            return null;
        }
        return payId.toString();
    }

    public static String  getPhoneNumber() {
        Object phoneNumber = LikeMobileThreadLocal.get("phoneNumber");
        if (phoneNumber == null || phoneNumber.toString().equals("")) {
            return null;
        }
        return phoneNumber.toString();
    }
    /**
     * 获取平台标识
     */
    public static Integer getTerminal() {
        Object adminId = LikeMobileThreadLocal.get("terminal");
        if (adminId == null || adminId.toString().equals("")) {
            return 0;
        }
        return Integer.parseInt(adminId.toString());
    }

    /**
     * 删除本地线程
     */
    public static void remove() {
        MY_LOCAL.remove();
    }

}
