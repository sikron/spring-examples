package com.skronawi.spring.examples.soap.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class ClientAppConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.blog.samples.webservices");
        return marshaller;
    }

    @Bean
    public AccountsClient accountsClient(Jaxb2Marshaller marshaller) {
        AccountsClient client = new AccountsClient();
        client.setDefaultUri("http://localhost:8080/endpoints/accountDetails.wsdl");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
