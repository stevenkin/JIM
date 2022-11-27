package com.github.stevenkin.jim.forward;

import com.github.stevenkin.serialize.Frame;
import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ForwardServerHandler extends SimpleChannelInboundHandler<Frame> {
    private List<ForwardProcessor> forwardProcessors = new ArrayList<>();

    public void registerProcessor(ForwardProcessor processor) {
        forwardProcessors.add(processor);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Frame msg) throws Exception {
        forwardProcessors.forEach( p -> {
            try {
                p.process(ctx, msg);
            } catch (Exception e) {
                log.error("forward process error", e);
            }
        });
    }
}
