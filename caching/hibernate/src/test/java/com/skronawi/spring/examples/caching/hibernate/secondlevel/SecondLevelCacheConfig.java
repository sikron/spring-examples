package com.skronawi.spring.examples.caching.hibernate.secondlevel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class SecondLevelCacheConfig {

    @Bean
    public Properties jpaProperties() {

        Properties properties = new Properties();

        properties.setProperty("hibernate.cache.use_second_level_cache", "true");
        properties.setProperty("hibernate.cache.use_minimal_puts", "true");

        properties.setProperty("hibernate.generate_statistics", "true");

        properties.setProperty("hibernate.cache.use_query_cache", "true");

        //hazelcast does not support hibernate 5
        //http://extendsobject.blogspot.com/2013/10/hibernate-caching-with-hazelcast.html
        //http://docs.hazelcast.org/docs/3.5/manual/html/hibernate.html
        //http://docs.hazelcast.org/docs/2.3/manual/html/ch13.html
//        properties.setProperty("hibernate.cache.hazelcast.use_lite_member", "true");
//        properties.setProperty("hibernate.cache.region.factory_class", "com.hazelcast.hibernate.HazelcastCacheRegionFactory");
////        properties.setProperty("hibernate.cache.region.factory_class", "com.hazelcast.hibernate.HazelcastLocalCacheRegionFactory");

        //use ehcache instead
        //https://examples.javacodegeeks.com/enterprise-java/hibernate/second-level-cache-hibernate-example/
        properties.setProperty("hibernate.current_session_context_class", "thread");
        properties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        properties.setProperty("net.sf.ehcache.configurationResourceName", "ehcache.xml");

        return properties;
    }
}
