package com.github.stevenkin.jim.discovery;

public interface ServiceNode {
    String serviceName();

    String nodeId();

    String getHost();

    int getPort();

    default <T> T getAttr(String name) {
        return null;
    }

    default boolean isPersistent() {
        return false;
    }

    default String hostAndPort() {
        return this.getHost() + ":" + this.getPort();
    }

    default String nodePath() {
        return this.serviceName() + '/' + this.nodeId();
    }
}
