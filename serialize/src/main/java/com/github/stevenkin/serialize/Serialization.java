package com.github.stevenkin.serialize;

import io.netty.buffer.ByteBuf;

public interface Serialization {
    ByteBuf serialize(PingPackage ping);

    ByteBuf serialize(PongPackage pong);

    ByteBuf serialize(LoginPackage login);

    Package deserialize(ByteBuf byteBuf);
}
