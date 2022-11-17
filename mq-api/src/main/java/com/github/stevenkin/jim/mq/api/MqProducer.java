package com.github.stevenkin.jim.mq.api;

import com.github.stevenkin.serialize.Package;

public interface MqProducer {
    void start() throws Exception;

    MqFuture send(Package pkg);

    void close() throws Exception;
}
