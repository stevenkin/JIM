package com.github.stevenkin.jim.business.server.business;

import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public interface BusinessHandler {
    boolean isSupport(Package pkg);

    void process(BusinessContext ctx,  Package pkg);
}
