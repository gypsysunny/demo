package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisPublisher {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost"); // 假设 Redis 服务器运行在本地
        System.out.println("Connected to Redis server!");

        // 发布消息到 "my-channel"
        jedis.publish("my-channel", "Hello, Redis Publish/Subscribe!");
        System.out.println("Message published to my-channel");

        jedis.close();
    }
}

