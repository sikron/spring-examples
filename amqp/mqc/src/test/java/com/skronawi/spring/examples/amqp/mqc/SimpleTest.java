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
    public void testPos() throws Exception {

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        final String id = producer.send("a");

        consumer.listen(new ConsumerCallback() {
            public void handle(MessageWithInfos messageWithInfos) {
                Assert.assertEquals(messageWithInfos.getId(), id);
                Assert.assertEquals(messageWithInfos.getPayload(), "a");
                Assert.assertEquals(messageWithInfos.getRetryCount(), 0);
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
    }

    @Test
    public void testNeg() throws Exception {

        /*
        deadletter_queue is configured as "deadletter" queue for working_queue, so the failed message is put there
        when the consumer fails.
        BUT actually the deadletter queue should only be used for TTL exceeded messages from the working queue.

        So the consumer must actively put the failed message into the retry-queue instead!
         */

        final CountDownLatch countDownLatch = new CountDownLatch(1);

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
