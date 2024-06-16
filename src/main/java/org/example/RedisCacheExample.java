package org.example;

import redis.clients.jedis.Jedis;

public class RedisCacheExample {

    public static void main(String[] args) {
        // 连接到Redis服务器，默认端口是6379
        Jedis jedis = new Jedis("localhost");

        try {
            // 验证Redis服务器是否运行正常
            System.out.println("Server is running: " + jedis.ping());

            // 设置缓存数据
            String key = "popular_product_info";
            String value = "{\"id\":123,\"name\":\"Popular Product\",\"price\":99.99}";
            jedis.set(key, value);
            System.out.println("Cached data: " + jedis.get(key));

            // 获取缓存数据
            String cachedData = jedis.get(key);
            if (cachedData != null) {
                System.out.println("Retrieved data from cache: " + cachedData);
            } else {
                System.out.println("No data found in cache for key: " + key);
            }

        } finally {
            // 关闭连接
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
