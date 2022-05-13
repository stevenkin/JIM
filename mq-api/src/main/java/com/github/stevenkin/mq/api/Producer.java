package com.github.stevenkin.mq.api;

import com.github.stevenkin.serialize.Package;

public interface Producer {
    void start() throws Exception;

    MqFuture send(Package pkg);

    void close() throws Exception;
}
