package com.github.stevenkin.jim.forward.client;

public interface ForwardCluster {
    ForwardClient choose(String key);
}
