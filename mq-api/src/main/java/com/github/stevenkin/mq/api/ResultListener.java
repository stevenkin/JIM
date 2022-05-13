package com.github.stevenkin.mq.api;

public interface ResultListener {
    void onResult(MqFuture future);
}
