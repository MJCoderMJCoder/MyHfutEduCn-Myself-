package com.lzf.myhfuteducn.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * SharedPreferences(保存用户偏好参数)保存数据
 *
 * @author MJCoder
 * @see SharedPreferences
 */
public class SharedPreferencesUtil {
    /**
     * 保存在手机里的 SharedPreferences 文件名
     */
    public static final String FILE_NAME = "SharedPreferencesUtil";

    /**
     * 向SharedPreferences中保存数据
     *
     * @param context 环境/上下文
     * @param key     保存的键
     * @param obj     保存的值
     */
    public static void put(Context context, String key, Object obj) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        } else {
            editor.putString(key, (String) obj);
        }
        editor.commit();
    }

    /**
     * 获取SharedPreferences中指定键的数据
     *
     * @param context    环境/上下文
     * @param key        指定的键
     * @param defaultObj 默认值
     * @return
     */
    public static Object get(Context context, String key, Object defaultObj) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        if (defaultObj instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObj);
        } else if (defaultObj instanceof Float) {
            return sp.getFloat(key, (Float) defaultObj);
        } else if (defaultObj instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObj);
        } else if (defaultObj instanceof Long) {
            return sp.getLong(key, (Long) defaultObj);
        } else if (defaultObj instanceof String) {
            return sp.getString(key, (String) defaultObj);
        }
        return null;
    }

    /**
     * 删除SharedPreferences中指定键的数据
     *
     * @param context 环境/上下文
     * @param key     指定的键
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 获取SharedPreferences中的所有键值对
     *
     * @param context 环境/上下文
     * @return 所有键值对
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        Map<String, ?> map = sp.getAll();
        return map;
    }

    /**
     * 删除SharedPreferences中的所有数据
     *
     * @param context 环境/上下文
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 检查SharedPreferences中指定键对应的数据是否存在
     *
     * @param context 环境/上下文
     * @param key     指定的键
     * @return 指定键对应的数据是否存在（true：指定键对应的数据已存在；false：指定键对应的数据不存在）
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, context.MODE_PRIVATE);
        return sp.contains(key);
    }
}
