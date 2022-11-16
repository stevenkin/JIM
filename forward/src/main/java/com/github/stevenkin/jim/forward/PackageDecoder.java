package com.github.stevenkin.jim.forward;

import com.github.stevenkin.serialize.Package;
import com.github.stevenkin.serialize.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class PackageDecoder extends MessageToMessageDecoder<ByteBuf> {
    private Serialization serialization;

    public PackageDecoder(Serialization serialization) {
        this.serialization = serialization;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        Package pkg = serialization.deserialize(byteBuf);
        out.add(pkg);
    }
}
