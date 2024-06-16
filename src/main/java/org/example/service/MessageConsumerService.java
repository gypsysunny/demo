package org.example.service;

import java.util.concurrent.TimeUnit;

import io.lettuce.core.RedisCommandTimeoutException;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.example.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


/**
 * ApplicationRunner 实现这个接口可以跟随项目启动而启动
 * @author crush
 */
@Service
@Log4j2
public class MessageConsumerService extends Thread {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private volatile boolean flag = true;

    private String queueKey="queue";

    private Long popTime=1000L;

    public Message popQueue() {
        try {
            if(redisTemplate.opsForList().size(queueKey) > 0) {
                return (Message) redisTemplate.opsForList().rightPop(queueKey, popTime, TimeUnit.SECONDS);
            }
            else {
                log.info("no elements in the {}", queueKey);
                return null;
            }
        } catch (RedisCommandTimeoutException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    @Override
    public void run() {
        try {
            Message message;
            // 为了能一直循环而不结束
            while(flag && !Thread.currentThread().isInterrupted()) {
                message = (Message) redisTemplate.opsForList().rightPop(queueKey,popTime,TimeUnit.SECONDS);
                System.out.println("接收到了" + message);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}
