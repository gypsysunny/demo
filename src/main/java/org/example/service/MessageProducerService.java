package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.entity.Message;
import org.example.lock.RedisLockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MessageProducerService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    RedisLockUtil redisLockUtil;

    private String queueKey="queue";
    private String rKeyLock = "lock";
    private String requestId = "w835023482094";

    public Long sendMeassage(Message message) {
        //获取锁
        Boolean lock = redisLockUtil.tryLock(rKeyLock, requestId, 10);
        if (null != lock && lock) {
            try {
                log.info("发送了" + message);
                return redisTemplate.opsForList().leftPush(queueKey, message);
            } finally {
                //释放锁
                Boolean release = redisLockUtil.releaseLock(rKeyLock, requestId);
            }
        } else {
            log.warn("redis锁获取失败|{}|{}", rKeyLock, lock);
        }
        return -1L;
    }
    
}

