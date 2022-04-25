package com.github.stevenkin.serialize;

import io.netty.buffer.ByteBuf;

public interface Serialization {
    ByteBuf serialize(PingPackage ping);

    ByteBuf serialize(PongPackage pong);

    Package deserialize(ByteBuf byteBuf);
}
