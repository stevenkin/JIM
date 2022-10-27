package com.github.stevenkin.jim.discovery;

import java.util.List;

public interface Discovery {
    void start() throws Exception;

    void stop() throws Exception;

    List<ServiceNode> lookup(String serviceName);

    void subscribe(String path, ServiceListener listener);

    void unsubscribe(String path, ServiceListener listener);
}
