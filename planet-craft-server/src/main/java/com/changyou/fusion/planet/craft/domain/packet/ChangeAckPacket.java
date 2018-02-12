package com.changyou.fusion.planet.craft.domain.packet;

import java.util.Arrays;

/**
 * Packet
 * <p>
 * Created by zhanglei_js on 2018/2/11.
 */
public class ChangeAckPacket {

    private int faces[];

    private int color;

    public int[] getFaces() {
        return faces;
    }

    public void setFaces(int[] faces) {
        this.faces = faces;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "ChangeAckPacket{" +
                "faces=" + Arrays.toString(faces) +
                ", color=" + color +
                '}';
    }
}
