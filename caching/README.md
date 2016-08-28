# Description

This project is about Caching in Spring. There exist these modules:

* `impls`:
This is about the caching in Spring itself. A little business logic exists with caching annotations. Tests exist, which
can be executed with different caching implementations, e.g. in-memory, Hazelcast or Redis.
Here the cache-accesses are verified, so that the tests are green, shows, that the cache is used properly.
* `hibernate`:
Here i tried to get an understanding of the caching in hibernate, i.e. the first-level cache, second-level cache and 
query-cache. Several tests exist, but here actually the logs must be looked at to understand when a database call is 
performed and when not! The fact that the tests are green does actually show nothing!  
In some tests the 2L cache statistics are shown, which could be used for test assertions, though.

# Requirements

* for the Redis based caching in the `RedisCachingTest` a Redis instance is required. e.g. a Redis server on 
localhost:6379, e.g. in a vagrant box like in `https://github.com/JasonPunyon/redishobo`