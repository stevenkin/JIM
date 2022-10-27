package com.github.stevenkin.jim.mq.api;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

@Slf4j
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
            try {
                resultListener.onSuccess(this);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("resultListener error {}", e.getMessage());
            }
        }
    }

    public void setResult(SendResult result) {
        set(result);
        if (resultListener != null) {
            try {
                resultListener.onSuccess(this);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("resultListener error {}", e.getMessage());
            }
        }
    }

    public void setFailure(Exception exception) {
        setException(exception);
        if (resultListener != null) {
            try {
                resultListener.onFailure(this);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("resultListener error {}", e.getMessage());
            }
        }
    }
}
