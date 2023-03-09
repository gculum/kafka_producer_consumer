package com.example.kafka_demo.kafka;

import com.example.kafka_demo.kafka.model.Alert;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

public class AlertLevelPartitioner implements Partitioner {
    public int partition(final String topic,
                         final Object objectKey,
                         final byte[] keyBytes,
                         final Object value,
                         final byte[] valueBytes,
                         final Cluster cluster) {
        final List<PartitionInfo> partitionInfoList =
                cluster.availablePartitionsForTopic(topic);
        final int partitionSize = partitionInfoList.size();
        final int criticalPartition = partitionSize - 1;
        final int partitionCount = partitionSize- 1;
        final String key = ((Alert) objectKey).getAlertLevel();
        if (key.contains("CRITICAL")) {
            return criticalPartition;
        } else {
            return Math.abs(key.hashCode()) % partitionCount;
        }
    }
    public void close() {
// nothing needed
    }
    public void configure(Map<String, ?> configs) {
// nothing needed
    }
}