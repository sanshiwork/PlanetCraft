package com.changyou.fusion.planet.craft.handler;

import com.changyou.fusion.planet.craft.socket.SessionWrapper;

/**
 * Handler
 * <p>
 * Created by zhanglei_js on 2018/2/11.
 */
public interface Handler {

    public static final String HANDLER_PREFIX = "PACKET_HANDLER_";

    public void handle(SessionWrapper session, int id, String data);
}
