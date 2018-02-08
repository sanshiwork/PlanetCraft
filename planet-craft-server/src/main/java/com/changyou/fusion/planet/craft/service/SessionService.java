package com.changyou.fusion.planet.craft.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 会话相关服务
 * Created by zhanglei_js on 2017/9/1.
 */
public interface SessionService {

    public HttpServletRequest getHttpServletRequest();

    public String ip();
}
