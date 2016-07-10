package com.skronawi.spring.examples.amqp.mqc;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class Producer {

    @Autowired
    private AmqpTemplate workingTemplate;
    @Autowired
    private AmqpTemplate retryTemplate;
    @Autowired
    private AmqpTemplate deadletterTemplate;

    @Value("${queue.retry.ttlsecs}")
    private int retryQueueTtlSecs;

    public String send(String message) {

        //TODO in case of problems, e.g. try 3 times to send with 10 seconds delay

        MessageWithInfos messageWithInfos = new MessageWithInfos();
        messageWithInfos.setId(UUID.randomUUID().toString());
        messageWithInfos.setPayload(message);

        System.out.println(new Date() + " sending message " + messageWithInfos.getId());
        workingTemplate.convertAndSend(messageWithInfos);
        return messageWithInfos.getId();
    }

    public void retry(final MessageWithInfos messageWithInfos) {

        System.out.println(new Date() + " re-sending message " + messageWithInfos.getId());
        messageWithInfos.setTryCount(messageWithInfos.getTryCount() + 1);
        retryTemplate.convertAndSend(messageWithInfos, new MessagePostProcessor() {
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties messageProperties = message.getMessageProperties();
                messageProperties.setRedelivered(true);
                int ttl = messageWithInfos.getTryCount() * retryQueueTtlSecs;
                System.out.println(new Date() + " with ttl seconds : " + ttl);
                messageProperties.setExpiration(String.valueOf(ttl * 1000));
                return message;
            }
        });
    }

    public void deadletter(MessageWithInfos messageWithInfos) {
        System.out.println(new Date() + " deadlettering message " + messageWithInfos.getId());
        deadletterTemplate.convertAndSend(messageWithInfos);
    }
}
