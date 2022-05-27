package com.github.stevenkin.jim.gateway.handler;

import com.github.stevenkin.jim.mq.api.MqFuture;
import com.github.stevenkin.jim.mq.api.Producer;
import com.github.stevenkin.jim.mq.api.SendResult;
import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import static com.github.stevenkin.serialize.Constant.SEND_FAILED;

@Slf4j
public class GatewayServerHandler extends SimpleChannelInboundHandler<Package> {
    private Producer producer;

    public GatewayServerHandler(Producer producer) {
        this.producer = producer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Package pkg) throws Exception {
        log.info("receive package {}", pkg);
        MqFuture future = producer.send(pkg);
        SendResult result = null;
        try {
            result = future.get();
        } catch (Exception e) {
            log.error("producer send message failed {}", e);
            pkg.getHeader().setCommand(SEND_FAILED);
            ctx.writeAndFlush(pkg);
            return;
        }
        log.info("producer send message success {}", result);

        String command = pkg.getHeader().getCommand();
        String ackCommand = command + "_ack";
        pkg.getHeader().setCommand(ackCommand);
        ctx.writeAndFlush(pkg);
    }
}
