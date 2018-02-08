package com.changyou.fusion.planet.craft.service.impl;

import com.changyou.fusion.planet.craft.socket.SessionWrapper;
import com.changyou.fusion.planet.craft.socket.Socket;
import com.changyou.fusion.planet.craft.service.FactoryService;
import org.springframework.stereotype.Service;

import javax.websocket.Session;

/**
 * FactoryServiceImpl
 * <p>
 * Created by zhanglei_js on 2018/2/8.
 */
@Service
public class FactoryServiceImpl implements FactoryService {
    @Override
    public SessionWrapper newSessionWrapper(Session session, Socket socket) {
        return new SessionWrapper(session, socket);
    }
}
