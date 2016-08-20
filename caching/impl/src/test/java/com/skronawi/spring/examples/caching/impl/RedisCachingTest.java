package com.skronawi.spring.examples.caching.impl;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RedisCacheManagerConfig.class)
public class RedisCachingTest extends AbstractCachingTest {
}
