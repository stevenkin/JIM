package com.github.stevenkin.jim.business.server.business;

import com.github.stevenkin.serialize.Package;

public interface BusinessHandler {
    boolean isSupport(Package pkg);

    Package process(Package pkg);
}
