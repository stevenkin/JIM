package com.github.stevenkin.jim.discovery;

public interface ServiceListener {
    void onServiceAdded(String path, ServiceNode serviceNode);

    void onServiceUpdated(String path, ServiceNode serviceNode);

    void onServiceRemoved(String path, ServiceNode serviceNode);
}
