package com.github.stevenkin.jim.mq.api;

public interface ResultListener {
    void onSuccess(MqFuture future) throws Exception;

    void onFailure(MqFuture future) throws Exception;
}
