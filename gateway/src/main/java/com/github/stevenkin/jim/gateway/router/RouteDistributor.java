package com.github.stevenkin.jim.gateway.router;

import com.github.stevenkin.serialize.Frame;
import com.github.stevenkin.serialize.Package;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//TODO 需要重新实现
@Component
@Slf4j
public class RouteDistributor implements InitializingBean, DisposableBean {
    @Autowired
    private RouteStrategy routeStrategy;
    @Autowired
    private BusinessServerCluster businessServerCluster;
    @Value("${jim.route.mq-consumer-thread}")
    private Integer mqConsumerThread;

    private ExecutorService mqConsumerService;

    @Override
    public void destroy() throws Exception {
        mqConsumerService.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mqConsumerService = Executors.newFixedThreadPool(mqConsumerThread);
    }

    public void start() {
        for (int i = 0; i < mqConsumerThread; i++) {
            mqConsumerService.submit(() -> {
                for (; ;) {
                    try {
                        //TODO 传输失败重传
                        Package aPackage = null;
                        List<Server> cluster = businessServerCluster.getCluster();
                        Set<Server> servers = routeStrategy.choose((Set<Server>) cluster, aPackage);
                        servers.stream().filter(Server::isActive).forEach(s -> {
                            Frame frame = explanationFrame(aPackage, s.info());
                            s.channel().writeAndFlush(frame).addListener(f -> {

                            });
                            //TODO 传输失败重传
                        });
                    } catch (Exception e) {
                        log.error("poll msg from mq error", e);
                    }
                }
            });
        }
    }

    private Frame explanationFrame(Package pkg, ServerInfo serverInfo) {
        Frame frame = new Frame();
        Frame.Control control = new Frame.Control();
        control.setSourceIP(serverInfo.getClientIP());
        control.setSourcePort(serverInfo.getClientPort());
        control.setDestAppName(serverInfo.getAppName());
        control.setDestAppToken(serverInfo.getAppToken());
        frame.setControl(control);
        frame.setOpCode(3);
        frame.setPayload(pkg);
        return frame;
    }
}
