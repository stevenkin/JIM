package com.github.stevenkin.jim.business.server.config;

import com.github.stevenkin.jim.business.server.BusinessNettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessServerConfig {
    @Autowired
    private BusinessServerProperties properties;
    @Bean
    public BusinessNettyServer businessNettyServer() {
        return new BusinessNettyServer(properties);
    }
}
