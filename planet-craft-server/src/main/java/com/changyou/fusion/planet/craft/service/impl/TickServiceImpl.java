package com.changyou.fusion.planet.craft.service.impl;

import com.changyou.fusion.planet.craft.service.SocketService;
import com.changyou.fusion.planet.craft.service.TickService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * TickServiceImpl
 * <p>
 * Created by zhanglei_js on 2018/2/8.
 */
@Service
public class TickServiceImpl implements TickService {

    private final static Logger logger = LoggerFactory.getLogger(TickServiceImpl.class);

    private long time;

    @Autowired
    private SocketService socketService;

    @Override
    public void tick() {
        long delta = System.currentTimeMillis() - time;

        // 处理所有输入
        tickInputs(10);

        // 处理所有输出
        tickOutputs(100);

        time = System.currentTimeMillis();
    }

    private void tickInputs(int size) {
        socketService.sessions().forEach((s, sessionWrapper) -> {
            for (int i = 0; i < size; i++) {
                String message = sessionWrapper.getInputs().poll();
                if (message != null) {
                    // 广播
                    socketService.sessions().values().forEach(session -> session.getOutputs().add(message));
                } else {
                    break;
                }
            }
        });

    }

    private void tickOutputs(int size) {
        socketService.sessions().forEach((s, sessionWrapper) -> {
            for (int i = 0; i < size; i++) {
                String message = sessionWrapper.getOutputs().poll();
                if (message != null) {
                    // 广播
                    try {
                        sessionWrapper.getSession().getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        logger.error(e.getLocalizedMessage(), e);
                    }
                } else {
                    break;
                }

            }
        });
    }


}
