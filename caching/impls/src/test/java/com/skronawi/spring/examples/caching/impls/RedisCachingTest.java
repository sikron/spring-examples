package com.skronawi.spring.examples.caching.impls;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RedisCacheManagerConfig.class)
public class RedisCachingTest extends AbstractCachingTest {
}
