package org.example.controller;

import org.example.entity.Message;
import org.example.entity.response.ResponseResult;
import org.example.service.IRedisService;
import org.example.service.MessageConsumerService;
import org.example.service.MessageProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author
 */
@RestController
@RequestMapping("/RedisMessage")
public class MessageController {

    @Autowired
    private IRedisService<Message> redisService;

    @Autowired
    MessageProducerService msgProducer;

    @Autowired
    MessageConsumerService msgConsumer;

    private final Long popTime=1000L;

    /**
     * @param Message Message param
     * @return message
     */
    @PostMapping("add")
    public ResponseResult<Message> add(Message msg) {
        redisService.set(String.valueOf(msg.getId()), msg);
        return ResponseResult.success(redisService.get(String.valueOf(msg.getId())));
    }

    @PostMapping("addQueue")
    public ResponseResult<Long> addQueue(Message message) {
        return ResponseResult.success(msgProducer.sendMeassage(message));
    }

    /**
     * @return message list
     */
    @GetMapping("find/{msgId}")
    public ResponseResult<Message> edit(@PathVariable("msgId") String msgId) {
        return ResponseResult.success(redisService.get(msgId));
    }

    @GetMapping("popQueue")
    public ResponseResult<Message> popQueue() {
        return ResponseResult.success(msgConsumer.popQueue());
    }
}
