package org.example.consumer;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

/**
 * @description 定时启动关闭监听器
 * @auth
 * @date
 */
@Component
@Slf4j
public class consumerCron {

    /**
     * 定时监听器消费
     * @param record
     * @author
     * @date
     * @return
     */
    @KafkaListener(id = "timingConsumer", topicPartitions = {
            @TopicPartition(topic = "first", partitionOffsets = {
                    @PartitionOffset(partition = "0", initialOffset = "0")
            }),
    },containerFactory = "delayContainerFactory")
    public void onMessage1(ConsumerRecord<?, ?> record){
        log.info("消费成功："+record.topic()+"-"+record.partition()+"-"+record.value() + "__" + DateUtil.date());
    }
}