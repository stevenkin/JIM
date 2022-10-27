package com.github.stevenkin.jim.forward.client;

import com.github.stevenkin.serialize.Frame;

public interface ForwardClient {
    void connect(String ip, int port) throws Exception;

    void send(Frame frame) throws Exception;

    void disconnect() throws Exception;
}
