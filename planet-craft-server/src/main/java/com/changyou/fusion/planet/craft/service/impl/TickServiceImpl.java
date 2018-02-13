package com.changyou.fusion.planet.craft.service.impl;

import com.changyou.fusion.planet.craft.domain.packet.Packet;
import com.changyou.fusion.planet.craft.handler.Handler;
import com.changyou.fusion.planet.craft.service.CacheService;
import com.changyou.fusion.planet.craft.service.SceneService;
import com.changyou.fusion.planet.craft.service.SocketService;
import com.changyou.fusion.planet.craft.service.TickService;
import com.changyou.fusion.planet.craft.socket.SessionStatus;
import com.changyou.fusion.planet.craft.task.GameEventEngine;
import com.changyou.fusion.planet.craft.util.ApplicationContextProvider;
import com.changyou.fusion.planet.craft.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Autowired
    private CacheService cacheService;

    @Autowired
    private SceneService sceneService;

    /**
     * 状态（0:保存结束,1:准备保存,2:正在保存）
     */
    private AtomicInteger save = new AtomicInteger(0);

    private GameEventEngine engine = new GameEventEngine(BUFFER_SIZE, POOL_SIZE);

    // 异步任务并发数
    private static final int POOL_SIZE = 10;

    // 环形队列大小数
    private static final int BUFFER_SIZE = 1024;

    @PostConstruct
    public void init() {
        engine = new GameEventEngine(BUFFER_SIZE, POOL_SIZE);
        engine.start();
    }

    @Override
    public void tick() {
        long delta = System.currentTimeMillis() - time;

        // 处理状态
        tickStatus();

        // 处理所有输入
        tickInputs(10);

        // 处理所有输出
        tickOutputs(100);

        // 异步任务
        tickTask(10);

        time = System.currentTimeMillis();
    }

    /**
     * save
     */
    @Override
    public void save() {
        if (save.get() == 0) {
            save.set(1);
        }
    }

    private void tickTask(int size) {
        // 每个Tick处理10个异步回调
        engine.callback(size);

        // 场景存库
        if (save.get() == 1) {
            save.set(2);
            engine.publish(params -> {
                logger.info("DB Saving.");
                cacheService.cache(CacheService.PREFIX.PLANET.getValue() + "array", (String) params[0]);
                return null;
            }, results -> {
                save.set(0);
                logger.info("DB Save finish.");
            }, JSON.toJson(sceneService.getPlanetFaces()));
        }
    }

    private void tickStatus() {
        socketService.sessions().forEach((s, sessionWrapper) -> {
            if (sessionWrapper.getStatus() == SessionStatus.INIT) {
                // 发送当前画布状态
                Packet packet = new Packet();
                packet.setId(Packet.INIT);
                packet.setData(sceneService.getPlanetFaces());
                sessionWrapper.getOutputs().add(JSON.toJson(packet));
                sessionWrapper.setStatus(SessionStatus.WAITING);
            }
        });
    }

    private void tickInputs(int size) {
        socketService.sessions().forEach((s, sessionWrapper) -> {
            if (sessionWrapper.getStatus() == SessionStatus.INIT) {
                // 抛弃所有的消息包
                sessionWrapper.getInputs().clear();
                return;
            }

            for (int i = 0; i < size; i++) {
                String message = sessionWrapper.getInputs().poll();
                if (message != null) {
                    Packet packet = JSON.fromJson(message, Packet.class);
                    // 忽略心跳包
                    if (packet.getId() < 0) {
                        Packet heartbeat = new Packet();
                        heartbeat.setId(Packet.HEARTBEAT);
                        heartbeat.setData(null);
                        sessionWrapper.getOutputs().add(JSON.toJson(heartbeat));
                        continue;
                    }

                    if (sessionWrapper.getStatus() == SessionStatus.WAITING && packet.getId() == Packet.INIT_ACK) {
                        ((Handler) ApplicationContextProvider.getBean(Handler.HANDLER_PREFIX + packet.getId())).handle(sessionWrapper, packet.getId(), packet.getData().toString());
                        break;
                    }

                    if (sessionWrapper.getStatus() == SessionStatus.CONNECTED) {
                        ((Handler) ApplicationContextProvider.getBean(Handler.HANDLER_PREFIX + packet.getId())).handle(sessionWrapper, packet.getId(), packet.getData().toString());
                    }
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
