package com.github.stevenkin.jim.gateway.handler;

import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GatewayServerHandler extends SimpleChannelInboundHandler<Package> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Package pkg) throws Exception {
        log.info("receive package {}", pkg);
    }
}
