package com.skronawi.spring.example.amqp.qpid;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(classes = EmbeddedBrokerTest.Config.class)
public class EmbeddedBrokerTest extends AbstractTestNGSpringContextTests {

    private static final String Q_NAME = "a_qpid_queue";

    @Configuration
    @PropertySource("classpath:broker.properties")
    public static class Config {

        @Value("${connection.host}")
        private String host;
        @Value("${connection.port}")
        private int port;
        @Value("${connection.username}")
        private String username;
        @Value("${connection.password}")
        private String password;
        @Value("${connection.vhost}")
        private String vhost;

        @Bean(destroyMethod = "stop", initMethod = "start")
        public EmbeddedBroker embeddedBroker() throws Exception {
            EmbeddedBroker embeddedBroker = new EmbeddedBroker(port, vhost, username, password);
            return embeddedBroker;
        }

        @Bean
        @DependsOn("embeddedBroker")
        public ConnectionFactory connectionFactory() {
            CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
            cachingConnectionFactory.setHost(host);
            cachingConnectionFactory.setPort(port);
            cachingConnectionFactory.setVirtualHost(vhost);
            cachingConnectionFactory.setUsername(username);
            cachingConnectionFactory.setPassword(password);
            return cachingConnectionFactory;
        }

        @Bean
        public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
            RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
            return rabbitAdmin;
        }

        @Bean
        public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
            RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
            return rabbitTemplate;
        }

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }

        @Bean
        public Queue queue() {
            return QueueBuilder.durable(Q_NAME).build();
        }
    }

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void testSendReceive() {

        String message = "this is a test message over qpid";

        amqpTemplate.convertAndSend(Q_NAME, message);
        String receivedMessage = (String) amqpTemplate.receiveAndConvert(Q_NAME, 1000);

        Assert.assertEquals(receivedMessage, message);
    }
}
