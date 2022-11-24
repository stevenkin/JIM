package com.github.stevenkin.jim.router;

import com.github.stevenkin.jim.forward.ForwardProcessor;
import com.github.stevenkin.jim.forward.ForwardServer;
import com.github.stevenkin.jim.mq.api.MqFuture;
import com.github.stevenkin.jim.mq.api.MqProducer;
import com.github.stevenkin.jim.mq.api.ResultListener;
import com.github.stevenkin.serialize.Package;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RouteInboundServer implements ForwardProcessor, ApplicationContextAware, InitializingBean, DisposableBean {
    @Autowired
    private ForwardServer forwardServer;
    @Autowired
    private MqProducer mqProducer;

    private ApplicationContext applicationContext;

    @Override
    public void destroy() throws Exception {
        forwardServer.close();
        mqProducer.close();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mqProducer.start();
        forwardServer.registerProcessor(this);
        forwardServer.open();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void process(ChannelHandlerContext ctx, Package msg) throws Exception {
        MqFuture future = mqProducer.send(msg);
        future.setResultListener(new ResultListener() {
            @Override
            public void onSuccess(MqFuture future) throws Exception {
                log.debug("send package to mq success {}", msg);
            }

            @Override
            public void onFailure(MqFuture future) throws Exception {
                log.error("send package to mq fail {}", msg);
            }
        });
    }
}
