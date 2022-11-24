package com.github.stevenkin.jim.router;

import java.util.Set;

public interface RouteStrategy {
    Set<ServerInfo> choose(Set<ServerInfo> serverSet);
}
