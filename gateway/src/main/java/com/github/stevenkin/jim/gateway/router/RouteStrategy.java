package com.github.stevenkin.jim.gateway.router;

import com.github.stevenkin.serialize.Package;

import java.util.List;
import java.util.Set;

public interface RouteStrategy {
    List<Server> choose(List<Server> serverSet, Package pkg);
}
