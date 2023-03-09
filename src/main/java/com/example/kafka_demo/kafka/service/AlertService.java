package com.example.kafka_demo.kafka.service;

import com.example.kafka_demo.kafka.AlertCallback;
import com.example.kafka_demo.kafka.model.Alert;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Configuration
@Service
public class AlertService {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${alert_topic}")
    private String alertTopic;


    private Producer<Alert, String> producer;

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapAddress);
        props.put("key.serializer", "com.example.kafka_demo.kafka.AlertKeySerde");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("partitioner.class", "com.example.kafka_demo.kafka.AlertLevelPartitioner");
        producer = new KafkaProducer<Alert, String>(props);
    }


    public boolean send(Alert alert) {
        ProducerRecord<Alert, String> producerRecord = new ProducerRecord<Alert, String>(alertTopic, alert, alert.getAlertMessage()); //
        //producer.send(producerRecord, new AlertCallback());

        try {
            RecordMetadata metadata = producer.send(producerRecord).get(2, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            //TODO error handling
            return false;
        }
        return true;
    }

}
