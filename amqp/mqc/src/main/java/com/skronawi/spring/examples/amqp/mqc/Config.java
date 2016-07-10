package com.skronawi.spring.examples.amqp.mqc;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
//@PropertySource("classpath:mqc.properties")
@ComponentScan("com.skronawi.spring.examples.amqp.mqc")
@Import(ConnectionConfig.class)
public class Config {

//    @Value("${connection.host}")
//    private String host;
//    @Value("${connection.port}")
//    private int port;
//    @Value("${connection.username}")
//    private String username;
//    @Value("${connection.password}")
//    private String password;
//    @Value("${connection.vhost}")
//    private String vhost;

    @Value("${queue.working.name}")
    private String workingQueueName;
    @Value("${queue.retry.name}")
    private String retryQueueName;
    @Value("${queue.deadletter.name}")
    private String deadletterQueueName;

    @Value("${queue.working.ttlsecs}")
    private int workingQueueTtlSecs;

    @Value("${queue.retry.ttlsecs}")
    private int retryQueueTtlSecs;

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
//        cachingConnectionFactory.setHost(host);
//        cachingConnectionFactory.setPort(port);
//        cachingConnectionFactory.setVirtualHost(vhost);
//        cachingConnectionFactory.setUsername(username);
//        cachingConnectionFactory.setPassword(password);
//        return cachingConnectionFactory;
//    }
//
//    @Bean
//    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
//        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
//        return rabbitAdmin;
//    }
//
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }

    @Bean(name = "workingTemplate")
    public AmqpTemplate workingTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setRoutingKey(workingQueueName + "_queue");
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean(name = "retryTemplate")
    public AmqpTemplate retryTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setRoutingKey(retryQueueName + "_queue");
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean(name = "deadletterTemplate")
    public AmqpTemplate deadletterTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setRoutingKey(deadletterQueueName + "_queue");
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean(name = "workingQueue")
    public Queue workingQueue() {
        return QueueBuilder.durable(workingQueueName + "_queue")

                /*
                the producer sends 1 message every second. so the result will be, that in working_queue
                there will always be the last 10 messages, and the deadletter-queue all older messages
                will be gathered by rabbitMq itself
                 */
                .withArgument("x-dead-letter-exchange", deadletterQueueName + "_exchange")

                //also, because it is direct and not fanout,
                //see http://stackoverflow.com/questions/28286334/spring-amqp-nothing-showing-up-in-dead-letter-queue
                .withArgument("x-dead-letter-routing-key", deadletterQueueName + "_queue")

                .withArgument("x-message-ttl", workingQueueTtlSecs * 1000)
                .build();
    }

    @Bean(name = "workingExchange")
    public DirectExchange workingExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(workingQueueName + "_exchange").durable().build();
    }

    @Bean(name = "workingBinding")
    public Binding workingBinding(Queue workingQueue, DirectExchange workingExchange) {
        return BindingBuilder.bind(workingQueue).to(workingExchange).with(workingQueueName + "_queue");
    }

    @Bean(name = "deadletterQueue")
    public Queue deadletterQueue() {
        return QueueBuilder.durable(deadletterQueueName + "_queue").build();
    }

    @Bean(name = "deadletterExchange")
    public DirectExchange deadletterExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(deadletterQueueName + "_exchange").durable().build();
    }

    @Bean(name = "deadletterBinding")
    public Binding deadletterBinding(Queue deadletterQueue, DirectExchange deadletterExchange) {
        return BindingBuilder.bind(deadletterQueue).to(deadletterExchange).with(deadletterQueueName + "_queue");
    }

    @Bean(name = "retryQueue")
    public Queue retryQueue() {
        return QueueBuilder.durable(retryQueueName + "_queue")
                .withArgument("x-dead-letter-exchange", workingQueueName + "_exchange")
                .withArgument("x-dead-letter-routing-key", workingQueueName + "_queue")
//                .withArgument("x-message-ttl", retryQueueTtlSecs * 1000) // set the TTL per message in this queue, e.g. exp-backoff
                .build();
    }

    @Bean(name = "retryExchange")
    public DirectExchange retryExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(retryQueueName + "_exchange").durable().build();
    }

    @Bean(name = "retryBinding")
    public Binding retryBinding(Queue retryQueue, DirectExchange retryExchange) {
        return BindingBuilder.bind(retryQueue).to(retryExchange).with(retryQueueName + "_queue");
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, ConsumerMessageListener consumerMessageListener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(workingQueueName + "_queue");
        container.setAcknowledgeMode(AcknowledgeMode.AUTO); //when listener returns normally, a confirm is sent
        container.setDefaultRequeueRejected(false); //do not requeue automatically, as we want to use the retry-queue

//        container.setErrorHandler(errorHandler); //errorHandler will user producer to re-queue
        // TODO can't this be configured? on error -> retry-queue

        container.setMessageConverter(new Jackson2JsonMessageConverter());
        container.setMessageListener(consumerMessageListener);

        return container;
    }

    @Bean
    public ConsumerMessageListener consumerMessageListener() {
        return new ConsumerMessageListener();
    }
}
