package com.changyou.fusion.planet.craft.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Json序列化/反序列化
 * Created by zhanglei_js on 2017/9/21.
 */
public class JSON {

    /**
     * gson
     */
    private static final Gson gson = new GsonBuilder().create();

    /**
     * JSON字符串转换为POJO对象
     *
     * @param <T>      class
     * @param json     json
     * @param classOfT classOfT
     * @return pojo t
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }


    /**
     * To json string.
     *
     * @param object the object
     * @return the string
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }


}
