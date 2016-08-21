package com.skronawi.spring.examples.caching.impls;

import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.*;

@Configuration
@EnableCaching
@ComponentScan
public class RedisCacheManagerConfig {

    /*
    see http://stackoverflow.com/questions/28247584/cache-with-redis-cache-manager-redistemplate-and-multiple-serializers
    but does not work
     */

//    private class CustomRedisCacheManager extends RedisCacheManager {
//
//        private static final String CACHE_NAME_DEFAULT = "default";
//
//        private Map<String, RedisCache> redisCaches = new HashMap<>();
//
//        public CustomRedisCacheManager(Map<String, RedisTemplate> redisTemplates) {
//            super(redisTemplates.get(CACHE_NAME_DEFAULT), redisTemplates.keySet());
//
//            redisTemplates.keySet().stream()
//                    .forEach(cacheName -> redisCaches.put(cacheName,
//                            new RedisCache(cacheName, null, redisTemplates.get(cacheName), 0)));
//        }
//
//        @Override
//        public Cache getCache(String name) {
//            return super.getCache(name);
//        }
//    }
//
//    @Bean
//    public CacheManager cacheManager(RedisTemplate itemRedisTemplate, RedisTemplate itemsRedisTemplate) {
//
//        HashMap<String, RedisTemplate> templates = new HashMap<>();
//        templates.put(ItemManager.ITEM_CACHE_NAME, itemRedisTemplate);
//        templates.put(ItemManager.ITEMS_CACHE_NAME, itemsRedisTemplate);
//
//        CustomRedisCacheManager cacheManager = new CustomRedisCacheManager(templates);
//        // Number of seconds before expiration. Defaults to unlimited (0)
//        cacheManager.setDefaultExpiration(300);
//        return cacheManager;
//    }

    @Primary
    @Bean
    public CacheManager itemCacheManager(RedisTemplate itemRedisTemplate){
        RedisCacheManager redisCacheManager = new RedisCacheManager(itemRedisTemplate);
        return redisCacheManager;
    }

    @Bean
    public CacheManager itemsCacheManager(RedisTemplate itemsRedisTemplate){
        RedisCacheManager redisCacheManager = new RedisCacheManager(itemsRedisTemplate);
        return redisCacheManager;
    }

    @Bean
    public RedisTemplate<String, Item> itemRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Item> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Item.class));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Set<Item>> itemsRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Set<Item>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(
                TypeFactory.defaultInstance().constructCollectionType(Set.class, Item.class)));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("127.0.0.1");
        jedisConnectionFactory.setPort(6379);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }
}
