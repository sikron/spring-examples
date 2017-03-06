package com.skronawi.spring.example.amqp.qpid;

import org.apache.qpid.server.Broker;
import org.apache.qpid.server.BrokerOptions;

import java.net.URL;
import java.nio.file.Paths;

/*
- http://stackoverflow.com/questions/30918557/embedded-amqp-java-broker
- https://dzone.com/articles/mocking-rabbitmq-for-integration-tests
 */
public class EmbeddedBroker {

    private static final String CONFIG_FILE = "qpid-config.json";

    private final int port;
    private final String vhost;
    private final String username;
    private final String password;

    private Broker broker;

    public EmbeddedBroker(int port, String vhost, String username, String password) {
        this.port = port;
        this.vhost = vhost;
        this.username = username;
        this.password = password;
    }

    public void start() throws Exception {

        broker = new Broker();
        BrokerOptions brokerOptions = new BrokerOptions();

        brokerOptions.setConfigProperty("qpid.port", String.valueOf(port));
        brokerOptions.setConfigProperty("qpid.username", username);
        brokerOptions.setConfigProperty("qpid.password", password);
        brokerOptions.setConfigProperty("qpid.vhost", vhost);

        brokerOptions.setConfigProperty("qpid.broker.defaultPreferenceStoreAttributes", "{\"type\": \"Noop\"}");
        brokerOptions.setConfigurationStoreType("Memory");
        brokerOptions.setInitialConfigurationLocation(findResourcePath(CONFIG_FILE));
//        brokerOptions.setStartupLoggedToSystemOut(false);

        broker.startup(brokerOptions);
    }

    public void stop() {
        if (broker != null) {
            broker.shutdown();
        }
    }

    private String findResourcePath(String file) throws Exception {
        URL resource = EmbeddedBroker.class.getResource("/" + file);
        return Paths.get(resource.toURI()).toAbsolutePath().toString();
    }
}
