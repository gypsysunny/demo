package org.example;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

@SpringBootApplication
@Slf4j
public class App implements CommandLineRunner {
    @Resource
    KafkaTemplate<String, String> kafkaTemplate;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args){
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("topic1", "11111");
        kafkaTemplate.send(producerRecord);
    }

    /**
     * 创建 topic
     * @author
     * @date
     * @param
     * @return org.apache.kafka.clients.admin.NewTopic
     **/
    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("topic1")
                .partitions(4)
                .replicas(1)
                .build();
    }

    /**
     * 批量自动确认ACK
     **/
    @KafkaListener(id = "consumer_1", topics = "topic1",groupId="wx_public_number",containerFactory = "batchFactory")
    public void consumer_1(List<String> in) {
        log.debug("消费数据 {}",in.size());
    }

    /**
     * 批量手动确认ACK
     **/
    @KafkaListener(id = "consumer_2", topics = "topic1",groupId="wx_public_number",containerFactory = "batchFactoryAck")
    public void consumer_2(List<String> in, Acknowledgment ack) {
        log.debug("消费数据 {}",in.size());
        ack.acknowledge();
    }

}
