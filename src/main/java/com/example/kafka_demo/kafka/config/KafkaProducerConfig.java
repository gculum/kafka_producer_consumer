package com.example.kafka_demo.kafka.config;


import com.example.kafka_demo.kafka.AlertLevelPartitioner;
import com.example.kafka_demo.kafka.model.Alert;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Properties;

@Configuration
public class KafkaProducerConfig extends KafkaConfig {
    @Bean
    public Producer<Alert, String> getAlertProducer() {
        Properties producerProps = createProducerProperties();
        Producer<Alert, String> producer = new KafkaProducer<Alert, String>(producerProps);
        return producer;
    }

    private Properties createProducerProperties() {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, AlertLevelPartitioner.class.getName());
        producerProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return producerProps;
    }
}