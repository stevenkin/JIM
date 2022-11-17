package com.github.stevenkin.jim.mq.redis.test;

import com.github.stevenkin.jim.mq.api.MqProducer;
import com.github.stevenkin.serialize.Package;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApp.class)
public class MqProducerTest {
    @Autowired
    private MqProducer mqProducer;

    @Test
    public void test() throws Exception {
        mqProducer.start();

        Package pkg = new Package("c", "c", 1L, 1, 1, "s", "r", "start");
        mqProducer.send(pkg).get();

        Thread.sleep(1000);

        Package pkg1 = new Package("c", "c", 1L, 1, 1, "s", "r", "hello");
        mqProducer.send(pkg1).get();

        Thread.sleep(1000);

        Package pkg2 = new Package("c", "c", 1L, 1, 1, "s", "r", "world");
        mqProducer.send(pkg2).get();

        Thread.sleep(1000);

        Package pkg3 = new Package("c", "c", 1L, 1, 1, "s", "r", "end");
        mqProducer.send(pkg3).get();

        mqProducer.close();

    }
}
