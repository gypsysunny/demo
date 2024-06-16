package org.example;

import redis.clients.jedis.Jedis;
/*
在这个例子中，TaskProducer 类负责将任务推入名为 "task-queue" 的 Redis List 中，而 TaskConsumer 类则在一个无限循环中从该队列中取出任务。brpop 方法是阻塞式的，它会等待直到队列中有新的元素可用。
        请注意，TaskConsumer 的 run 方法中的无限循环需要谨慎处理，因为在实际应用中，你可能需要一种机制来优雅地停止消费者线程（例如，通过设置一个停止标志或使用中断）。此外，如果你有多个消费者线程，它们将并发地从队列中取出任务，从而实现真正的并行处理。
        最后，别忘了在生产环境中处理好异常和连接管理。你可能还需要考虑使用连接池来管理 Redis 连接，特别是在高并发的场景中。

 */
public class TaskConsumer implements Runnable {
    private Jedis jedis;
    private final String queueKey = "task-queue";

    public TaskConsumer(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public void run() {
        while (true) { // 无限循环，直到程序被外部终止
            String task = jedis.brpop(0, queueKey).get(1); // 从队列右侧（尾部）取出并移除任务，阻塞直到有任务可用
            if (task != null) {
                System.out.println("Consumed task: " + task);
                // 在这里处理任务...
                // ...
            }
        }
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost"); // 假设 Redis 服务器运行在本地
        TaskConsumer consumer = new TaskConsumer(jedis);

        // 在新的线程中运行消费者，以便它可以持续地从队列中取出任务
        new Thread(consumer).start();
    }
}
