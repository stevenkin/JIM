package com.github.stevenkin.jim.mq.redis;

import com.github.stevenkin.jim.mq.api.MqFuture;
import com.github.stevenkin.jim.mq.api.Producer;
import com.github.stevenkin.jim.mq.api.SendResult;
import com.github.stevenkin.serialize.Package;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RedisMqProducer implements Producer {
    private RedisTemplate<String, Package> redisTemplate;

    private String topic;

    private boolean isClose;

    public RedisMqProducer(RedisTemplate<String, Package> redisTemplate, String topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
        this.isClose = false;
    }

    @Override
    public void start() throws Exception {
        this.isClose = false;
    }

    @Override
    public MqFuture send(Package pkg) {
        if (isClose) {
            throw new UnsupportedOperationException("producer is closed");
        }
        redisTemplate.opsForList().rightPush(topic, pkg);
        MqFuture mqFuture = new MqFuture();
        mqFuture.setResult(new SendResult("OK", new HashMap<>()));
        return mqFuture;
    }

    @Override
    public void close() throws Exception {
        this.isClose = true;
    }
}
