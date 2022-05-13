package com.github.stevenkin.jim.mq.redis;

import com.github.stevenkin.jim.mq.api.Consumer;
import com.github.stevenkin.serialize.Package;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class RedisMqConsumer implements Consumer {
    private RedisTemplate<String, Package> redisTemplate;

    public RedisMqConsumer(RedisTemplate<String, Package> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void subscribe(List<String> topics) {

    }

    @Override
    public Package poll(int mills) throws Exception {
        return null;
    }

    @Override
    public Package poll() throws Exception {
        return null;
    }
}
