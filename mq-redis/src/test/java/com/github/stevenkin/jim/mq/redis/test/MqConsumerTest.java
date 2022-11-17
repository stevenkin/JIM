package com.github.stevenkin.jim.mq.redis.test;

import com.github.stevenkin.jim.mq.api.MqConsumer;
import com.github.stevenkin.serialize.Package;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApp.class)
public class MqConsumerTest {
    @Autowired
    private MqConsumer mqConsumer;

    @Test
    public void test() throws Exception {
        mqConsumer.start();

        for (;;) {
            Package pkg = mqConsumer.poll();
            System.out.println(pkg);
            if (pkg.getBody().equals("end")) {
                break;
            }
        }

    }
}
