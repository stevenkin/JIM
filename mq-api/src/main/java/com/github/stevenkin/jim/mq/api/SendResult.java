package com.github.stevenkin.jim.mq.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendResult {
    private String msg;

    private Map<String, Object> attach;
}
