package com.github.stevenkin.serialize;

import io.netty.buffer.ByteBuf;

public interface Serialization<T> {
    ByteBuf serialize(T msg, ByteBuf byteBuf);

    T deserialize(ByteBuf byteBuf);
}
