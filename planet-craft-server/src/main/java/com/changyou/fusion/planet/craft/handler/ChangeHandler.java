package com.changyou.fusion.planet.craft.handler;

import com.changyou.fusion.planet.craft.domain.packet.ChangeAckPacket;
import com.changyou.fusion.planet.craft.domain.packet.ChangePacket;
import com.changyou.fusion.planet.craft.domain.packet.Packet;
import com.changyou.fusion.planet.craft.service.SceneService;
import com.changyou.fusion.planet.craft.service.SocketService;
import com.changyou.fusion.planet.craft.socket.SessionWrapper;
import com.changyou.fusion.planet.craft.util.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ChangeHandler
 * Created by zhanglei_js on 2018/2/11.
 */
@Component(Handler.HANDLER_PREFIX + Packet.CHANGE)
public class ChangeHandler implements Handler {

    @Autowired
    private SceneService sceneService;

    @Autowired
    private SocketService socketService;

    @Override
    public void handle(SessionWrapper session, int id, String data) {
        ChangePacket packet = JSON.fromJson(data, ChangePacket.class);
        sceneService.updatePlanetFace(packet.getFace(), packet.getColor());

        // 响应内容
        int[] faces;
        if (packet.getPencil() == 0) {
            faces = new int[2];
            faces[0] = packet.getFace();
            if (packet.getFace() % 2 == 0) {
                sceneService.updatePlanetFace(packet.getFace() + 1, packet.getColor());
                faces[1] = packet.getFace() + 1;
            } else {
                sceneService.updatePlanetFace(packet.getFace() - 1, packet.getColor());
                faces[1] = packet.getFace() - 1;
            }
        } else {
            faces = new int[1];
            faces[0] = packet.getFace();
        }

        Packet ack = new Packet();
        ack.setId(Packet.CHANGE_ACK);
        ChangeAckPacket ackData = new ChangeAckPacket();
        ackData.setColor(packet.getColor());
        ackData.setFaces(faces);
        ack.setData(ackData);

        // 广播消息包
        socketService.broadcast(ack);

    }
}
