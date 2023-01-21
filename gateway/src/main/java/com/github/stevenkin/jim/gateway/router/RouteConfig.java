package com.github.stevenkin.jim.gateway.router;

import com.github.stevenkin.jim.forward.ForwardServer;
import com.github.stevenkin.serialize.FrameSerialization;
import com.github.stevenkin.serialize.PackageSerialization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    @Value("${router.port}")
    private Integer port;

    @Bean
    public ForwardServer forwardServer() {
        return new ForwardServer(port, new FrameSerialization());
    }
}
