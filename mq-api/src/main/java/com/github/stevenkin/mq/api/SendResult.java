package com.github.stevenkin.mq.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendResult {
    private boolean success;

    private String msg;

    private Map<String, Object> attach;

    private Exception exception;
}
