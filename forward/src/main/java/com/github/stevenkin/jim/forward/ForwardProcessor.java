package com.github.stevenkin.jim.forward;

import com.github.stevenkin.serialize.Frame;
import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelHandlerContext;

public interface ForwardProcessor {
    void process(ChannelHandlerContext ctx, Frame msg) throws Exception;
}
