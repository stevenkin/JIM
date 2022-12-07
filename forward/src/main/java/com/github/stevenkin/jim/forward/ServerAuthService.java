package com.github.stevenkin.jim.forward;

public interface ServerAuthService {
    boolean auth(String appName, String appToken);
}
