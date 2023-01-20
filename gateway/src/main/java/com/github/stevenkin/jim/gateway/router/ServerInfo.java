package com.github.stevenkin.jim.gateway.router;

import lombok.Data;

@Data
public class ServerInfo {
    private String appName;

    private String appToken;

    private int appWaitTaskNum;

    private int appThreadNum;

    private String clientIP;

    private int clientPort;
}
