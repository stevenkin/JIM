package com.github.stevenkin.serialize;

import lombok.Data;

@Data
public class Frame {
    //0x1:登录请求，0x2:登录响应，0x3:数据传输，0x4:数据传输响应, 0x5:心跳, 0x6:心跳响应, 0x7:下线，0x8:下线响应
    private int opCode;

    private Control control;

    private Package payload;

    @Data
    public static class Control {
        private String clientIP;

        private int clientPort;

        private String appName;

        private String appToken;

        private long timestamp;

        private int appWaitTaskNum;

        private int appThreadNum;
    }
}
