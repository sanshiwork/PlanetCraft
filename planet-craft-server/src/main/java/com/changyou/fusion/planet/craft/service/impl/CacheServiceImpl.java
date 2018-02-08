package com.changyou.fusion.planet.craft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 缓存相关服务实现
 * <p>
 * Created by zhanglei_js on 2017/9/4.
 */
@Service
public class CacheServiceImpl implements com.changyou.fusion.planet.craft.service.CacheService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public void cache(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void inc(String key, long value) {
        redisTemplate.opsForValue().increment(key, value);
    }

    @Override
    public void expire(String key, String value, long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    @Override
    public void expire(String key, long time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }
}
