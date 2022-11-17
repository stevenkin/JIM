package com.github.stevenkin.jim.business.server.config;

import com.github.stevenkin.jim.business.server.BusinessDistributor;
import com.github.stevenkin.jim.mq.api.MqConsumer;
import com.github.stevenkin.jim.mq.api.MqProducer;
import com.github.stevenkin.serialize.Serialization;
import com.github.stevenkin.serialize.SimpleSerialization;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.concurrent.ExecutorService;

@Configuration
public class BusinessServerConfig implements EnvironmentAware, ApplicationContextAware {
    private Environment environment;

    private ApplicationContext applicationContext;

    @Bean
    public Serialization serialization() {
        return new SimpleSerialization();
    }

    @Bean
    public BusinessDistributor businessDistributor(Serialization serialization) throws ClassNotFoundException {
        String mqConsumerImpl = environment.getProperty("mqConsumer", "com.github.stevenkin.jim.mq.redis.RedisMqMqConsumer");
        MqConsumer consumer =(MqConsumer) (applicationContext.getBean(Class.forName(mqConsumerImpl)));
        return new BusinessDistributor(consumer, applicationContext);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
