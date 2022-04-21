package com.github.stevenkin.jim.gateway.config;

import com.github.stevenkin.jim.gateway.server.WebsocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Autowired
    private GatewayProperties properties;

    @Bean
    public WebsocketServer websocketServer() {
        return new WebsocketServer(properties);
    }
}
