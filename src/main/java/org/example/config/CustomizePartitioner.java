package org.example.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @description 自定义分区规则，需要在配置中指定当前类生效
 * @auth
 * @date
 */
@Component
public class CustomizePartitioner implements Partitioner {

    /**
     * 自定义分区策略
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 获取topic的分区列表
        List<PartitionInfo> partitionInfoList = cluster.availablePartitionsForTopic(topic);
        int partitionCount = partitionInfoList.size();
        int auditPartition = partitionCount - 1;
        return auditPartition;
    }

    /**
     * 在分区程序关闭时调用
     */
    @Override
    public void close() {
        System.out.println("colse ...");
    }

    /**
     * 做必要的初始化工作
     */
    @Override
    public void configure(Map<String, ?> configs) {
        System.out.println("init ...");
    }
}
