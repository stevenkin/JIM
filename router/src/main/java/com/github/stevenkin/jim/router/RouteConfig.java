package com.github.stevenkin.jim.router;

import com.github.stevenkin.jim.forward.ForwardServer;
import com.github.stevenkin.jim.mq.api.MqProducer;
import com.github.stevenkin.jim.mq.redis.RedisMqMqProducer;
import com.github.stevenkin.serialize.SimpleSerialization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    @Value("${server.port}")
    private Integer port;

    @Bean
    public ForwardServer forwardServer() {
        return new ForwardServer(port, new SimpleSerialization());
    }
}
