package com.github.stevenkin.jim.mq.redis;

import com.github.stevenkin.jim.mq.api.MqConsumer;
import com.github.stevenkin.jim.mq.api.MqProducer;
import com.github.stevenkin.serialize.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ConditionalOnClass({MqProducer.class, MqConsumer.class})
@ConditionalOnProperty(prefix = "spring.redis", value = "enabled", matchIfMissing = true)
@Import(RedisConfig.class)
public class MqRedisAutoConfiguration {
    @Autowired
    private RedisTemplate<String, Package> redisTemplate;
    @Value("${jim.mq.redis.topic}")
    private String topic;

    @Bean
    public MqProducer producer() {
        return new RedisMqMqProducer(redisTemplate, topic);
    }

    @Bean
    public MqConsumer consumer() {
        return new RedisMqMqConsumer(redisTemplate, topic);
    }
}
