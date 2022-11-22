package com.github.stevenkin.jim.forward;

import com.github.stevenkin.serialize.Package;
import com.github.stevenkin.serialize.Serialization;
import com.github.stevenkin.serialize.SimpleSerialization;
import org.junit.Test;

public class ForwardClientTest {
    private Serialization serialization;

    public ForwardClientTest() {
        this.serialization = new SimpleSerialization();
    }

    @Test
    public void test() throws Exception {
        ForwardClient client = new ForwardClient(serialization);
        client.connect("127.0.0.1", 8090);
        Package aPackage = new Package("123", "123", 1L, 2, 3, "abc", "abc", "abc");
        client.send(aPackage);
        client.disconnect();
    }
}
