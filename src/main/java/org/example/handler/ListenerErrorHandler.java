package org.example.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @description 消费异常处理器
 * @auth
 * @date
 */
@Component
@Slf4j
public class ListenerErrorHandler {
    /**
     * 异常处理器
     * @author
     * @date
     */
    @Bean
    public ConsumerAwareListenerErrorHandler myConsumerAwareErrorHandler() {
        return new ConsumerAwareListenerErrorHandler() {
            @Override
            public Object handleError(Message<?> message, ListenerExecutionFailedException exception,
                                      Consumer<?, ?> consumer) {
                log.info("--- 发生消费异常 ---");
                log.info((String)message.getPayload());
                log.error("group id: {}, exception message: {}", exception.getGroupId(),exception.getMessage());
                return null;
            }
        };
    }
}
