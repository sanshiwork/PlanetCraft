package com.changyou.fusion.planet.craft.socket;

import com.changyou.fusion.planet.craft.service.FactoryService;
import com.changyou.fusion.planet.craft.service.SocketService;
import com.changyou.fusion.planet.craft.util.ApplicationContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * Socket
 * <p>
 * Created by zhanglei_js on 2018/2/7.
 */
@ServerEndpoint(value = "/socket")
@Component
public class Socket {

    private final static Logger logger = LoggerFactory.getLogger(Socket.class);

    private SocketService socketService;

    private FactoryService factoryService;

    private String uuid;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        logger.info("Close Connection:{}", session.getId());
        socketService = ApplicationContextProvider.getBean(SocketService.class);
        factoryService = ApplicationContextProvider.getBean(FactoryService.class);
        uuid = session.getId();
        socketService.connect(factoryService.newSessionWrapper(session, this));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        logger.info("Close Connection:{}", uuid);
        socketService.disconnect(uuid);
        socketService = null;
        factoryService = null;
        uuid = null;

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        socketService.receive(session.getId(), message);
    }


    /**
     * 异常处理
     *
     * @param session   session
     * @param throwable throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error(session.getId(), throwable);
    }
}
