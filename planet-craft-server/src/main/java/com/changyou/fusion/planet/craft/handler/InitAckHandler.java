package com.changyou.fusion.planet.craft.handler;

import com.changyou.fusion.planet.craft.domain.packet.Packet;
import com.changyou.fusion.planet.craft.socket.SessionStatus;
import com.changyou.fusion.planet.craft.socket.SessionWrapper;
import org.springframework.stereotype.Component;

/**
 * InitHandler
 * Created by zhanglei_js on 2018/2/11.
 */
@Component(Handler.HANDLER_PREFIX + Packet.INIT_ACK)
public class InitAckHandler implements Handler {

    @Override
    public void handle(SessionWrapper session, int id, String data) {
        session.setStatus(SessionStatus.CONNECTED);

    }
}
