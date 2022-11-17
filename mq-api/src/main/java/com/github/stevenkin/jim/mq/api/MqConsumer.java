package com.github.stevenkin.jim.mq.api;

import com.github.stevenkin.serialize.Package;

import java.util.List;

public interface MqConsumer {
    void start() throws Exception;

    Package poll(int mills) throws Exception;

    Package poll() throws Exception;

    void close() throws Exception;
}
