package com.skronawi.spring.examples.caching.impls;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes =  HazelcastCacheManagerConfig.class)
public class HazelcastCachingTest extends AbstractCachingTest{
}
