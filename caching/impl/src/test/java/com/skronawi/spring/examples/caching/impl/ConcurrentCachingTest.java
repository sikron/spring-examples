package com.skronawi.spring.examples.caching.impl;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = ConcurrentMapCacheManagerConfig.class)
public class ConcurrentCachingTest extends AbstractCachingTest {
}
