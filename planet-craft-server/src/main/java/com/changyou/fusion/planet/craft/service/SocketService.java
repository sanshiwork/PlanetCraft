package com.changyou.fusion.planet.craft.service;

import com.changyou.fusion.planet.craft.socket.SessionWrapper;

import java.util.Map;

/**
 * SocketService
 * <p>
 * Created by zhanglei_js on 2018/2/8.
 */
public interface SocketService {

    /**
     * 增加连接
     *
     * @param sessionWrapper sessionWrapper
     */
    public void connect(SessionWrapper sessionWrapper);

    /**
     * 关闭连接
     *
     * @param uuid uuid
     */
    public void disconnect(String uuid);

    /**
     * 收到消息
     *
     * @param uuid    uuid
     * @param message message
     */
    public void receive(String uuid, String message);

    /**
     * 发送消息
     *
     * @param uuid    uuid
     * @param message message
     */
    public void send(String uuid, String message);


    /**
     * 获取所有连接
     *
     * @return sessions
     */
    public Map<String, SessionWrapper> sessions();
}
