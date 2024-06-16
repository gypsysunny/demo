package org.example.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * @description 自定义分区规则，需要在配置中指定当前类生效
 * @auth
 * @date
 */
public class CustomizePartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object o, byte[] bytes, Object value, byte[] bytes1, Cluster cluster) {
        String msg = value.toString();
        int partition = 0;
        // 消息种包含hello，就发往1号分区
        if(msg.contains("hello")){
            partition = 1;
        }
        return 0;
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> map) {
    }

}
