package com.skronawi.spring.examples.caching.impls;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@EnableCaching
@Configuration
public class HazelcastCacheManagerConfig {

    @Bean
    public HazelcastInstance hazelcastInstance() {
        /*
        no config here, only defaults.
        e.g. see
        - http://docs.hazelcast.org/docs/latest/manual/html-single/index.html#spring-integration
        - https://dzone.com/articles/configuring-hazelcast-within
         */
        return Hazelcast.newHazelcastInstance();
    }

    @Bean
    public CacheManager cacheManager() {
        return new HazelcastCacheManager(hazelcastInstance());
    }
}
