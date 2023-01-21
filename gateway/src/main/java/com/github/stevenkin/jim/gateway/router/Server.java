package com.github.stevenkin.jim.gateway.router;

import com.github.stevenkin.serialize.Package;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

public interface Server {
    void update(ServerInfo info);

    boolean isActive();

    Channel channel();

    Future write(Package pkg);

    ServerInfo info();
}
