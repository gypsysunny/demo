package org.example;

import redis.clients.jedis.Jedis;
/*
使用 Redis 的 List 数据类型作为任务队列是一个常见的做法。下面是一个简单的 Java 示例，展示如何使用 Jedis 库来实现一个基于 Redis List 的 FIFO（先进先出）任务队列。
        首先，确保你的 Redis 服务器正在运行，并且你已经在项目中添加了 Jedis 的依赖。
        然后，我们可以创建两个简单的类：一个用于将任务放入队列的 TaskProducer 和一个用于从队列中取出并处理任务的 TaskConsumer。
 */
public class TaskProducer {
    private Jedis jedis;
    private final String queueKey = "task-queue";

    public TaskProducer(Jedis jedis) {
        this.jedis = jedis;
    }

    public void produceTask(String task) {
        jedis.lpush(queueKey, task); // 将任务推入队列左侧（头部）
        System.out.println("Produced task: " + task);
    }

    public static void main(String[] args) {
        /*
        $ docker run -itd --name redis-test -p 6379:6379 redis
         */
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        TaskProducer producer = new TaskProducer(jedis);

        // 生产一些任务
        producer.produceTask("Task 1");
        producer.produceTask("Task 2");
        producer.produceTask("Task 3");

        // 关闭连接（在实际应用中，你可能希望在应用程序结束时关闭连接）
        jedis.close();
    }
}


