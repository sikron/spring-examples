package com.skronawi.spring.examples.caching.hibernate.firstlevel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class DefaultCacheConfig {

    @Bean
    public Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.cache.use_second_level_cache", "false");
        return properties;
    }
}
