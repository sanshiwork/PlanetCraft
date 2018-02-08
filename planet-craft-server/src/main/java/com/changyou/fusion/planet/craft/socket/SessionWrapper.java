package com.changyou.fusion.planet.craft.socket;

import javax.websocket.Session;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * SessionWrapper
 * <p>
 * Created by zhanglei_js on 2018/2/8.
 */
public class SessionWrapper {

    private Session session;

    private Socket socket;

    private Queue<String> inputs;

    private Queue<String> outputs;

    /**
     * Instantiates a new Session wrapper.
     *
     * @param session the session
     * @param socket  the socket
     */
    public SessionWrapper(Session session, Socket socket) {
        this.session = session;
        this.socket = socket;
        this.inputs = new ConcurrentLinkedQueue<>();
        this.outputs = new ConcurrentLinkedQueue<>();
    }

    /**
     * Gets session.
     *
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * Gets socket.
     *
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }


    /**
     * Receive.
     *
     * @param message the message
     */
    public void receive(String message) {
        inputs.add(message);
    }


    /**
     * Send.
     *
     * @param message the message
     */
    public void send(String message) {
        outputs.add(message);
    }

    /**
     * Gets inputs.
     *
     * @return the inputs
     */
    public Queue<String> getInputs() {
        return inputs;
    }

    /**
     * Gets outputs.
     *
     * @return the outputs
     */
    public Queue<String> getOutputs() {
        return outputs;
    }
}
