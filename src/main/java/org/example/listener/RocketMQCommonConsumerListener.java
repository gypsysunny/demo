package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 消费顺序消息
 * 配置RocketMQ监听
 *
 * ConsumeMode.ORDERLY:顺序消费
 * @author
 */
@Service
@RocketMQMessageListener(consumerGroup = "test",topic = "test-topic-orderly",consumeMode = ConsumeMode.ORDERLY)
@Slf4j
public class RocketMQCommonConsumerListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        log.info("consumer 顺序消费，收到消息:"+s);
    }
}



