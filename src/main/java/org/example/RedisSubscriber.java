package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisSubscriber extends JedisPubSub {
    @Override
    public void onMessage(String channel, String message) {
        System.out.println("Received message: " + message + " on channel: " + channel);
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost"); // 假设 Redis 服务器运行在本地
        System.out.println("Connected to Redis server!");

        // 订阅 "my-channel"
        new Thread(new Runnable() {
            @Override
            public void run() {
                jedis.subscribe(new RedisSubscriber(), "my-channel");
            }
        }).start();
    }
}
