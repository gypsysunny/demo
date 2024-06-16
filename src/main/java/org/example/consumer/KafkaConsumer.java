package org.example.consumer;

import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 消费者
 *
 * @author austin-brant
 * @since 2019/7/15 19:58
 */
@Slf4j
@Component
public class KafkaConsumer {

    //    @KafkaListener(topics = "${topic.name}")
    //    public void listen(ConsumerRecord<String, String> record) {
    //        consumer(record);
    //    }

    @KafkaListener(topics = {"${topic.name}"}, containerFactory = "batchFactory", id = "consumer")
    public void listen(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        log.info("batch listen size {}.", records.size());
        try {
            records.forEach(it -> consumer(it));
        } finally {
            ack.acknowledge();  //手动提交偏移量
        }
    }

    /**
     * 单条消费
     */
    public void consumer(ConsumerRecord<String, String> record) {
        log.info("主题:{}, 内容: {}", record.topic(), record.value());
    }
}
