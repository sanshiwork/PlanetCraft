package com.changyou.fusion.planet.craft.domain.packet;

/**
 * Packet
 * <p>
 * Created by zhanglei_js on 2018/2/11.
 */
public class ChangePacket {

    private int id;

    private int face;

    private int color;

    private int pencil;

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPencil() {
        return pencil;
    }

    public void setPencil(int pencil) {
        this.pencil = pencil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ChangePacket{" +
                "id=" + id +
                ", face=" + face +
                ", color=" + color +
                ", pencil=" + pencil +
                '}';
    }
}
