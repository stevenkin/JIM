package com.github.stevenkin.jim.forward;

public interface ForwardCluster {
    ForwardClient choose(String key);
}
