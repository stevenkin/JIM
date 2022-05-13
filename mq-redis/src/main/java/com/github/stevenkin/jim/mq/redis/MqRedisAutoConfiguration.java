package com.github.stevenkin.jim.mq.redis;

import com.github.stevenkin.jim.mq.api.Consumer;
import com.github.stevenkin.jim.mq.api.Producer;
import com.github.stevenkin.serialize.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnClass({Producer.class, Consumer.class})
@ConditionalOnProperty(prefix = "spring.redis", value = "enabled", matchIfMissing = true)
public class MqRedisAutoConfiguration {
    @Autowired
    private RedisTemplate<String, Package> redisTemplate;
    @Value("${spring.redis.topic}")
    private String topic;

    @Bean
    @ConditionalOnMissingBean(Producer.class)
    public Producer producer() {
        return new RedisMqProducer(redisTemplate, topic);
    }

    @Bean
    @ConditionalOnMissingBean(Consumer.class)
    public Consumer consumer() {
        return new RedisMqConsumer(redisTemplate);
    }
}
