package org.example.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @auth
 * @date
 */
@RestController
@RequestMapping("/kafka")
public class producer {
    private final static String topic = "first";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

//    @RequestMapping("/send")
//    public void send(String msg) {
//        // kafkaTemplate.send(topic, 1, "2" , msg);
//        kafkaTemplate.send(topic, msg).addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
//            @Override
//            public void onFailure(Throwable ex) {
//                System.out.println("发送消息失败：" + ex.getMessage());
//            }
//
//            @Override
//            public void onSuccess(SendResult<String, String> result) {
//                System.out.println("发送消息成功：" + result.getRecordMetadata().topic() + "-"
//                        + result.getRecordMetadata().partition() + "-" + result.getRecordMetadata().offset());
//            }
//        });
//    }

    /**
     * 生产者事务发送：需配置transaction-id-prefix开启事务
     *
     * @param msg 消息内容
     * @author
     * @date
     */
//    @Transactional
//    @RequestMapping("/transaction")
//    public void transaction(String msg) {
//        kafkaTemplate.send(topic, msg);
//        int a = 1 / 0;
//        kafkaTemplate.send(topic, "_____" + msg);
//    }

    /**
     * 第二种事务发送
     *
     * @param msg 消息内容
     * @author
     * @date
     */
    @RequestMapping("/transaction2")
    public void transaction2(String msg) {
        kafkaTemplate.executeInTransaction(new KafkaOperations.OperationsCallback() {
            @Override
            public Object doInOperations(KafkaOperations kafkaOperations) {
                kafkaOperations.send(topic, msg);
//                int a = 1 / 0;
                return true;
            }
        });
    }
}