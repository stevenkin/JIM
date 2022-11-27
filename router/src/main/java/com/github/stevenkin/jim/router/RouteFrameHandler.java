package com.github.stevenkin.jim.router;

import com.github.stevenkin.serialize.Frame;

public interface RouteFrameHandler {
    boolean isMatch(Frame frame);

    void handle(Frame frame) throws Exception;
}
