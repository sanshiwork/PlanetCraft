package com.changyou.fusion.planet.craft.task;

/**
 * 异步回调
 * <p>
 * Created by zhanglei_js on 2018/2/7.
 */
@FunctionalInterface
public interface Callback {
    void callback(Object... args);
}


