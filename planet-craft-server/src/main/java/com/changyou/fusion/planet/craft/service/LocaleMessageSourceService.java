package com.changyou.fusion.planet.craft.service;

/**
 * 国际化相关服务
 * <p>
 * Created by zhanglei_js on 2017/9/4.
 */
public interface LocaleMessageSourceService {

    public String getMessage(String code, Object... objects);

}
