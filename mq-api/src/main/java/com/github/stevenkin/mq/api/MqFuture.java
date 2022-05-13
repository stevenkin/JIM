package com.github.stevenkin.mq.api;

import java.util.concurrent.Future;

public abstract class MqFuture implements Future<SendResult> {
    private ResultListener resultListener;

    public void setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
    }
}
