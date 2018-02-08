package com.changyou.fusion.planet.craft.service;

import com.changyou.fusion.planet.craft.socket.SessionWrapper;
import com.changyou.fusion.planet.craft.socket.Socket;

import javax.websocket.Session;

/**
 * FactoryService
 * <p>
 * Created by zhanglei_js on 2018/2/8.
 */
public interface FactoryService {

    public SessionWrapper newSessionWrapper(Session session, Socket socket);
}
