package com.github.stevenkin.jim.business.server.config;

import com.github.stevenkin.jim.business.server.BusinessNettyServer;
import com.github.stevenkin.serialize.Serialization;
import com.github.stevenkin.serialize.SimpleSerialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessServerConfig {
    @Autowired
    private BusinessServerProperties properties;

    @Bean
    public Serialization serialization() {
        return new SimpleSerialization();
    }

    @Bean
    public BusinessNettyServer businessNettyServer(Serialization serialization) {
        return new BusinessNettyServer(properties, serialization);
    }
}
