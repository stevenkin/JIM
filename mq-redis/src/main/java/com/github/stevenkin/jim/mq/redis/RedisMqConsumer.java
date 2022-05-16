package com.github.stevenkin.jim.mq.redis;

import com.github.stevenkin.jim.mq.api.Consumer;
import com.github.stevenkin.serialize.Package;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisMqConsumer implements Consumer {
    private RedisTemplate<String, Package> redisTemplate;

    private String topic;

    private boolean isClose;

    public RedisMqConsumer(RedisTemplate<String, Package> redisTemplate, String topic) {
        this.redisTemplate = redisTemplate;
        this.isClose = true;
        this.topic = topic;
    }

    @Override
    public void start() throws Exception {
        this.isClose = false;
    }

    @Override
    public Package poll(int mills) throws Exception {
        if (StringUtils.isEmpty(topic)) {
            throw new UnsupportedOperationException("consumer need bind topic");
        }
        Package pkg = redisTemplate.opsForList().leftPop(topic, mills, TimeUnit.MILLISECONDS);
        return pkg;
    }

    @Override
    public Package poll() throws Exception {
        Package pkg = null;
        while ((pkg = poll(10000)) == null);
        return pkg;
    }
}
