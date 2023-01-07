package com.github.stevenkin.jim.router;

import com.github.stevenkin.jim.forward.ForwardProcessor;
import com.github.stevenkin.jim.forward.ForwardServer;
import com.github.stevenkin.serialize.Frame;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class RouteServer implements ForwardProcessor, ApplicationContextAware, InitializingBean, DisposableBean {
    @Autowired
    private ForwardServer forwardServer;
    @Autowired
    private RouteDistributor routeDistributor;

    private ApplicationContext applicationContext;

    private Set<RouteFrameHandler> routeFrameHandlers;

    @Override
    public void destroy() throws Exception {
        forwardServer.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        forwardServer.registerProcessor(this);
        forwardServer.open();
        Map<String, RouteFrameHandler> beansOfType = applicationContext.getBeansOfType(RouteFrameHandler.class);
        routeFrameHandlers = new HashSet<>(beansOfType.values());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void process(ChannelHandlerContext ctx, Frame msg) throws Exception {
        routeFrameHandlers.forEach(h -> {
            if (h.isMatch(msg)) {
                try {
                    h.handle(ctx, msg);
                } catch (Exception e) {
                    log.error(h.getClass() + " handle frame " + msg + "error", e);
                }
            }
        });
    }
}
