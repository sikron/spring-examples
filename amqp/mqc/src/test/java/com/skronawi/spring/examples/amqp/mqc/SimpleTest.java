package com.skronawi.spring.examples.amqp.mqc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.CountDownLatch;

@ContextConfiguration(classes = Config.class)
public class SimpleTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private Producer producer;

    @Autowired
    private Consumer consumer;

    @Test
    public void testSimplePositive() throws Exception {

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        final String id = producer.send("a");

        consumer.listen(new ConsumerCallback() {
            public void handle(MessageWithInfos messageWithInfos) {
                Assert.assertEquals(messageWithInfos.getId(), id);
                Assert.assertEquals(messageWithInfos.getPayload(), "a");
                Assert.assertEquals(messageWithInfos.getTryCount(), 0);
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void testTry3TimesThenDeadLetter() throws Exception {

        final CountDownLatch countDownLatch = new CountDownLatch(4);

        final String id = producer.send("a");

        consumer.listen(new ConsumerCallback() {
            public void handle(MessageWithInfos messageWithInfos) {
                try {
                    throw new IllegalStateException("test");
                } finally {
                    countDownLatch.countDown();
                }
            }
        });

        countDownLatch.await();
    }
}
