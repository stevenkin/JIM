package com.github.stevenkin.jim.business.server.handler;

import com.github.stevenkin.jim.business.server.business.BusinessHandler;
import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class BusinessDistributeHandler extends SimpleChannelInboundHandler<Package> {
    private ExecutorService executorService;

    private ApplicationContext applicationContext;

    private Collection<BusinessHandler> businessHandlers;

    public BusinessDistributeHandler(ApplicationContext applicationContext, ExecutorService executorService) {
        this.applicationContext = applicationContext;
        this.executorService = executorService;
        Map<String, BusinessHandler> beansOfType = applicationContext.getBeansOfType(BusinessHandler.class);
        Collection<BusinessHandler> values = beansOfType.values();
        businessHandlers = values;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Package msg) throws Exception {
        executorService.submit(() -> {
            if (businessHandlers != null && !businessHandlers.isEmpty()) {
                businessHandlers.forEach(b -> {
                    if (b.isSupport(msg)) {
                        b.process(ctx, msg);
                    }
                });
            }
        });
    }
}
