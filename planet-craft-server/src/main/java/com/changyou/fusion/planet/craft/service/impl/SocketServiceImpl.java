package com.changyou.fusion.planet.craft.service.impl;

import com.changyou.fusion.planet.craft.service.SocketService;
import com.changyou.fusion.planet.craft.socket.SessionWrapper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SocketServiceImpl
 * <p>
 * Created by zhanglei_js on 2018/2/8.
 */
@Service
public class SocketServiceImpl implements SocketService {

    /**
     * 全局连接
     */
    private final Map<String, SessionWrapper> sessions = new ConcurrentHashMap<>();

    @Override
    public void connect(SessionWrapper sessionWrapper) {
        sessions.put(sessionWrapper.getSession().getId(), sessionWrapper);
    }

    @Override
    public void disconnect(String uuid) {
        sessions.remove(uuid);
    }

    @Override
    public void receive(String uuid, String message) {
        sessions.get(uuid).receive(message);
    }

    @Override
    public void send(String uuid, String message) {
        sessions.get(uuid).send(message);
    }

    /**
     * 获取所有连接
     *
     * @return sessions
     */
    @Override
    public Map<String, SessionWrapper> sessions() {
        return this.sessions;
    }
}
