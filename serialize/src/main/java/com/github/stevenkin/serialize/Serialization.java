package com.github.stevenkin.serialize;

import io.netty.buffer.ByteBuf;

public interface Serialization {
    ByteBuf serialize(Package pkg);

    Package deserialize(ByteBuf byteBuf);
}
