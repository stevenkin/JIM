package com.github.stevenkin.jim.gateway.handler;

import com.github.stevenkin.jim.mq.api.Producer;
import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GatewayServerHandler extends SimpleChannelInboundHandler<Package> {
    private Producer producer;

    public GatewayServerHandler(Producer producer) {
        this.producer = producer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Package pkg) throws Exception {
        log.info("receive package {}", pkg);
        producer.send(pkg);

        String command = pkg.getHeader().getCommand();
        String ackCommand = command + "_ack";
        pkg.getHeader().setCommand(ackCommand);

        ctx.writeAndFlush(pkg);
    }
}
