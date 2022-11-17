package com.github.stevenkin.jim.gateway.config;

import com.github.stevenkin.jim.gateway.server.WebsocketServer;
import com.github.stevenkin.jim.gateway.service.EncryptKeyService;
import com.github.stevenkin.jim.mq.api.MqProducer;
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
    private MqProducer mqProducer;

    @Bean
    public WebsocketServer websocketServer() {
        return new WebsocketServer(properties, encryptKeyService, mqProducer);
    }
}
