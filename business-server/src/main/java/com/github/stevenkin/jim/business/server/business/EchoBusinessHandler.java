package com.github.stevenkin.jim.business.server.business;

import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EchoBusinessHandler implements BusinessHandler{
    @Override
    public boolean isSupport(Package pkg) {
        return pkg.getHeader().getCommand().equals("echo");
    }

    @Override
    public void process(ChannelHandlerContext ctx, Package pkg) {
        log.info("echoBusinessHandler receive package {}", pkg);
        ctx.writeAndFlush(pkg);
    }
}
