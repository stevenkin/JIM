package com.github.stevenkin.jim.gateway.router;

import com.github.stevenkin.serialize.Frame;
import io.netty.channel.ChannelHandlerContext;

public interface RouteFrameHandler {
    boolean isMatch(Frame frame);

    void handle(ChannelHandlerContext ctx, Frame frame) throws Exception;
}
