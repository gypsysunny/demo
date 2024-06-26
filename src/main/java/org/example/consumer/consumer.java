package org.example.consumer;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description
 * @auth
 * @date
 */
@Component
@Slf4j
public class consumer {

    /**
     * 简单发送
     * @param record
     * @author
     * @date
     * @return
     */
    @KafkaListener(topics = {"first"})
    public void listen(ConsumerRecord<?, ?> record) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("简单消费：" + record.topic() + "-" + record.partition() + "-" + record.value());
    }


    /**
     * 声明consumerID为demo，方便kafkaserver打印日志定位请求来源，监听topicName为topic.quick.demo的Topic
     * clientIdPrefix设置clientId前缀， idIsGroup id为groupId：默认为true
     * concurrency: 在监听器容器中运行的线程数,创建多少个consumer，值必须小于等于Kafk Topic的分区数。大于分区数时会有部分线程空闲
     * topicPattern 匹配Topic进行监听(与topics、topicPartitions 三选一)
     *
     * @param record 消息内容
     * @param ack    应答
     * @author
     * @date
     */
    @KafkaListener(id = "demo", topics = "first", groupId = "mykafka2", idIsGroup = false, clientIdPrefix = "myClient1", concurrency = "${listen.concurrency:3}")
    public void listen(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info(record.toString());
        log.info(record.value());
        // 消息处理下游，成功消费后提交ack
        // 手动提交offset
        ack.acknowledge();
    }


    /**
     * 指定offset位置消费
     * @param record
     * @param ack
     * @author
     * @date
     */
    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = "first", partitionOffsets = {
                    @PartitionOffset(partition = "0", initialOffset = "0")
            }),
    })
    public void listen2(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info(record.value());

        ack.acknowledge();
    }

    /**
     * 指定offset位置消费
     * 批量消费需配置 listener.type: batch
     * @param record
     * @param ack
     * @author
     * @date
     */
//    @KafkaListener(topicPartitions = {
//            @TopicPartition(topic = "first", partitionOffsets = {
//                    @PartitionOffset(partition = "0", initialOffset = "0")
//            }),
//    })
//    public void listen3(List<String> record, Acknowledgment ack) {
////    public void listen3(List<ConsumerRecord<String, String>> record, Acknowledgment ack) {
//        log.info(record.toString());
//        ack.acknowledge();
//    }


    /**
     * 通过 containerFactory过滤消息，批量消费
     *
     * @param record
     * @param ack
     * @author
     * @date
     */
    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = "first", partitionOffsets = {
                    @PartitionOffset(partition = "0", initialOffset = "0")
            }),
    }, errorHandler = "myConsumerAwareErrorHandler", containerFactory = "filterContainerFactory2")
    public void listen3(List<ConsumerRecord<String, String>> record, Acknowledgment ack) {

        log.info(record.toString());
        ack.acknowledge();
    }


    /**
     * @KafkaListener(groupId = "testGroup", topicPartitions = {
     *             @TopicPartition(topic = "topic1", partitions = {"0", "1"}),
     *             @TopicPartition(topic = "topic2", partitions = "0",
     *                     partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "100"))
     *     },concurrency = "6")
     *     */

    /**
     * 消息处理后转发到另一个topic
     * @author yh
     * @date 2022/5/11
     * @return
     */
    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = "first", partitionOffsets = {
                    @PartitionOffset(partition = "0", initialOffset = "0")
            }),
    })
    @SendTo("two")
    public String listen4(ConsumerRecord<String, String> record, Acknowledgment ack) {

        log.info("topic--first：" + record.value());
        // int a =1/0;
        ack.acknowledge();
        return record.value();
    }

    @KafkaListener(topics = "two")
    public void listentwo(ConsumerRecord<String, String> record, Acknowledgment ack){
        log.info("topic--two接收消息" + record.value());
        ack.acknowledge();
    }
}