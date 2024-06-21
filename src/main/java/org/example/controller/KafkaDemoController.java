package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/KafkaMessage")
@Slf4j
public class KafkaDemoController {

    @Autowired KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "NewTopic";

    @GetMapping("/publish/{message}")
    public String publishMessage(@PathVariable("message") final String message) {
        kafkaTemplate.send(TOPIC, message);
        return "Published Successfully";
    }

    @GetMapping("/send1")
    public String send1(String message) {
        kafkaTemplate.send(TOPIC, message).whenCompleteAsync((result, exp) -> {
            if (null != exp) {
                String message1 = exp.getMessage();
                log.info("exeption_info:{}", message1);
            } else {
                String topic = result.getRecordMetadata().topic();
                int partition = result.getRecordMetadata().partition();
                long offset = result.getRecordMetadata().offset();
                log.info("topic:" + topic + " partition:" + partition + " offset:" + offset);
            }
        });

        return "success";
    }
}
