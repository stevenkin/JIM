package com.github.stevenkin.jim.forward;

import com.github.stevenkin.serialize.SimpleSerialization;
import org.junit.Test;

public class ForwardServerTest {
    @Test
    public void test() throws InterruptedException {
        ForwardServerHandler forwardServerHandler = new ForwardServerHandler();
        forwardServerHandler.registerProcessor(new SimpleForwardProcessor());
        ForwardServer server = new ForwardServer(8090, forwardServerHandler, new SimpleSerialization());
        server.open();
        Thread.sleep(60000);
        server.close();
    }
}
