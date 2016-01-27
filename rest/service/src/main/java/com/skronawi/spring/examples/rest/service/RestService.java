package com.skronawi.spring.examples.rest.service;

import com.skronawi.spring.examples.jpa.api.DataPersistence;
import com.skronawi.spring.examples.jpa.api.ModelFactory;
import com.skronawi.spring.examples.rest.businesslogic.api.DataBusinessLogic;
import com.skronawi.spring.examples.rest.businesslogic.impl.SimpleDataBusinessLogic;
import com.skronawi.spring.examples.rest.inmemorypersistence.InMemoryDataPersistence;
import com.skronawi.spring.examples.rest.inmemorypersistence.InMemoryModelFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages = "com.skronawi.spring.examples.rest")
@EnableWebMvc
public class RestService {

    @Bean
    public DataBusinessLogic getDataBusinessLogic() {
        return new SimpleDataBusinessLogic();
    }

    @Bean
    public DataPersistence getDataPersistence() {
        return new InMemoryDataPersistence();
    }

    @Bean
    public ModelFactory getModelFactory() {
        return new InMemoryModelFactory();
    }
}
