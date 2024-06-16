package org.example;

import redis.clients.jedis.Jedis;

public class RedisCounter {
    private Jedis jedis;

    public RedisCounter(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 递增计数器
     * @param key 计数器的键
     * @return 递增后的值
     */
    public long increment(String key) {
        return jedis.incr(key);
    }

    /**
     * 获取计数器的当前值
     * @param key 计数器的键
     * @return 当前值，如果不存在则返回null
     */
    public Long get(String key) {
        return jedis.get(key) != null ? Long.parseLong(jedis.get(key)) : null;
    }

    // ... 其他可能的方法，如重置计数器等
}
