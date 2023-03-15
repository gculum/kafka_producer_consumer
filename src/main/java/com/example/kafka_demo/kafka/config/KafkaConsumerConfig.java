package com.example.kafka_demo.kafka.config;

import com.example.kafka_demo.kafka.model.Alert;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import java.util.Arrays;
import java.util.Properties;

@Configuration
public class KafkaConsumerConfig extends KafkaConfig {

    @Bean
    public Consumer<Alert, String> getAlertConsumer() {
        Properties producerProps = createConsumerProperties();
        Consumer<Alert, String> consumer = new KafkaConsumer<Alert, String>(producerProps);
        consumer.subscribe(Arrays.asList(alertTopic));
        return consumer;
    }

    private Properties createConsumerProperties() {
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "helloconsumer");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.KEY_DEFAULT_TYPE, Alert.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return consumerProps;
    }
}
