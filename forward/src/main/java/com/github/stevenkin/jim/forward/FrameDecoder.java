package com.github.stevenkin.jim.forward;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class FrameDecoder extends LengthFieldBasedFrameDecoder {
    public FrameDecoder() {
        super(10240, 0, 2, 0, 2);
    }
}
