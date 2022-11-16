package com.github.stevenkin.jim.forward;

import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ForwardServerHandler extends SimpleChannelInboundHandler<Package> {
    private ForwardProcessor forwardProcessor;

    public void registerProcessor(ForwardProcessor processor) {
        this.forwardProcessor = processor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Package msg) throws Exception {
        forwardProcessor.process(ctx, msg);
    }
}
