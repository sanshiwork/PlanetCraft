package com.changyou.fusion.planet.craft.task;

/**
 * 异步任务
 * <p>
 * Created by zhanglei_js on 2018/2/7.
 */
@FunctionalInterface
public interface Execute {
    Object[] execute(Object... args);
}


