package com.github.stevenkin.jim.gateway.config;

import com.github.stevenkin.jim.gateway.server.WebsocketServer;
import com.github.stevenkin.jim.gateway.service.EncryptKeyService;
import com.github.stevenkin.jim.mq.api.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Autowired
    private GatewayProperties properties;
    @Autowired
    private EncryptKeyService encryptKeyService;
    @Autowired
    private Producer producer;

    @Bean
    public WebsocketServer websocketServer() {
        return new WebsocketServer(properties, encryptKeyService, producer);
    }
}
