package com.github.stevenkin.jim.gateway.router;

import io.netty.channel.Channel;

public interface Server {
    void update(ServerInfo info);

    boolean isActive();

    Channel channel();

    ServerInfo info();
}
