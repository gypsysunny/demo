package org.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.example.entity.MemberMessage;
import org.example.handler.EnhanceMessageHandler;
import org.springframework.stereotype.Component;

/**
 * topic 是主题
 * consumerGroup 是消费者组，一条消息只能被同一个消费者组里的一个消费者消费。
 * selectorExpression 是用于消息过滤的，以 TAG 方式为例：
 * 默认为 "*"，表示不过滤，消费此 topic 下所有消息
 * 配置为 "tagA"，表示只消费此 topic 下 TAG = tagA 的消息
 * 配置为 "tagA || tagB"，表示消费此 topic 下 TAG = tagA 或  TAG = tagB 的消息，以此类推
 * 消费模式：默认 CLUSTERING （ CLUSTERING：负载均衡 ）（ BROADCASTING：广播机制 ）
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = "enhance_consumer_group",
        topic = "rocket_enhance",
        selectorExpression = "*",
//        selectorExpression = "tagA || tagB",
        messageModel = MessageModel.CLUSTERING,
        consumeThreadMax = 5 //默认是64个线程并发消息，配置 consumeThreadMax 参数指定并发消费线程数，避免太大导致资源不够
)
public class EnhanceMemberMessageListener extends EnhanceMessageHandler<MemberMessage> implements RocketMQListener<MemberMessage> {

    @Override
    protected void handleMessage(MemberMessage message) throws Exception {
        // 此时这里才是最终的业务处理，代码只需要处理资源类关闭异常，其他的可以交给父类重试
        System.out.println("业务消息处理:"+message.getUserName());
    }

    @Override
    protected void handleMaxRetriesExceeded(MemberMessage message) {
        // 当超过指定重试次数消息时此处方法会被调用
        // 生产中可以进行回退或其他业务操作
        log.error("消息消费失败，请执行后续处理");
    }


    /**
     * 是否执行重试机制
     */
    @Override
    protected boolean isRetry() {
        return true;
    }

    @Override
    protected boolean throwException() {
        // 是否抛出异常，false搭配retry自行处理异常
        return false;
    }

    @Override
    protected boolean filter() {
        // 消息过滤
        return false;
    }

    /**
     * 监听消费消息，不需要执行业务处理，委派给父类做基础操作，父类做完基础操作后会调用子类的实际处理类型
     */
    @Override
    public void onMessage(MemberMessage memberMessage) {
        super.dispatchMessage(memberMessage);
    }
}

