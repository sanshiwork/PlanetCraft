package com.changyou.fusion.planet.craft.domain.packet;

/**
 * Packet
 * <p>
 * Created by zhanglei_js on 2018/2/11.
 */
public class Packet {

    public static final int HEARTBEAT = -1;

    public static final int INIT = 0;

    public static final int INIT_ACK = 1;

    public static final int CHANGE = 2;

    public static final int CHANGE_ACK = 3;

    private int id;

    private Object data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "id=" + id +
                ", data=" + data +
                '}';
    }
}
