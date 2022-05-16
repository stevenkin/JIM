package com.github.stevenkin.jim.mq.redis.test;

import com.github.stevenkin.jim.mq.api.Consumer;
import com.github.stevenkin.serialize.Package;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApp.class)
public class ConsumerTest {
    @Autowired
    private Consumer consumer;

    @Test
    public void test() throws Exception {
        consumer.start();

        for (;;) {
            Package pkg = consumer.poll();
            System.out.println(pkg);
            if (pkg.getBody().equals("end")) {
                break;
            }
        }

    }
}
