package com.changyou.fusion.planet.craft.service;

import java.util.concurrent.TimeUnit;

/**
 * 缓存相关服务
 * Created by zhanglei_js on 2017/9/1.
 */
public interface CacheService {

    enum PREFIX {

        PLANET("planet:craft:");

        PREFIX(String value) {
            this.value = value;
        }

        // 获取前缀
        public String getValue() {
            return this.value;
        }

        // 前缀内容
        private String value;
    }

    public void cache(String key, String value);

    public void inc(String key, long value);

    public void expire(String key, String value, long time, TimeUnit timeUnit);

    public void expire(String key, long time, TimeUnit timeUnit);

    public String get(String key);

    public boolean hasKey(String key);

    public void remove(String key);


}
