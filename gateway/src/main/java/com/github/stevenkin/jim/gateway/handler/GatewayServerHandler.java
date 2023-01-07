package com.github.stevenkin.jim.gateway.handler;

import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import static com.github.stevenkin.serialize.Constant.SEND_FAILED;

@Slf4j
public class GatewayServerHandler extends SimpleChannelInboundHandler<Package> {

    public GatewayServerHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Package pkg) throws Exception {
        log.info("receive package {}", pkg);
        try {
        } catch (Exception e) {
            log.error("producer send message failed {}", e);
            pkg.getHeader().setCommand(SEND_FAILED);
            ctx.writeAndFlush(pkg);
            return;
        }

        String command = pkg.getHeader().getCommand();
        String ackCommand = command + "_ack";
        pkg.getHeader().setCommand(ackCommand);
        ctx.writeAndFlush(pkg);
    }
}
