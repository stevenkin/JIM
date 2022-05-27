package com.github.stevenkin.serialize;

import io.netty.buffer.ByteBuf;

public interface Serialization {
    ByteBuf serialize(Package pkg, ByteBuf byteBuf);

    Package deserialize(ByteBuf byteBuf);
}
