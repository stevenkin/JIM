package com.github.stevenkin.jim.mq.api;

public interface ResultListener {
    void onFinish(MqFuture future) throws Exception;
}
