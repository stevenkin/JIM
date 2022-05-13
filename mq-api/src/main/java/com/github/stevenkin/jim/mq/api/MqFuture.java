package com.github.stevenkin.jim.mq.api;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MqFuture extends FutureTask<SendResult> {
    private ResultListener resultListener;

    public MqFuture() {
        super(new Callable<SendResult>() {
            @Override
            public SendResult call() throws Exception {
                return null;
            }
        });
    }

    public void setResultListener(ResultListener resultListener) {
        this.resultListener = resultListener;
        if (isDone()) {
            resultListener.onFinish(this);
        }
    }

    public void setResult(SendResult result) {
        set(result);
        if (resultListener != null) {
            resultListener.onFinish(this);
        }
    }

    public void setFailure(Exception exception) {
        setException(exception);
        if (resultListener != null) {
            resultListener.onFinish(this);
        }
    }
}
