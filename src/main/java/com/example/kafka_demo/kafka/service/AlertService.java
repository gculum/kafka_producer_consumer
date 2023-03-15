package com.example.kafka_demo.kafka.service;

import com.example.kafka_demo.kafka.model.Alert;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Configuration
@Service
public class AlertService {
    @Value("${alert_topic}")
    private String alertTopic;
    @Autowired
    private Producer<Alert, String> alertProducer;
    @Autowired
    private Consumer<Alert, String> alertConsumer;

    @Override
    protected void finalize() {
        //consumer.close();
    }

    public boolean send(Alert alert) {
        try {
            ProducerRecord<Alert, String> producerRecord =
                    new ProducerRecord<Alert, String>(alertTopic, alert, alert.getAlertMessage());
            RecordMetadata metadata = alertProducer.send(producerRecord).get(2, TimeUnit.SECONDS);
            System.out.println("Added to partition " + metadata.partition());
            System.out.println("Offset " + metadata.offset());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            //TODO error handling
            return false;
        }
        return true;
    }

    public List<Alert> getAlertsFromKafka() {
        List<Alert> alerts = new ArrayList<>();
        ConsumerRecords<Alert, String> records = alertConsumer.poll(Duration.ofMillis(3000));
        for (ConsumerRecord<Alert, String> record : records) {
            alerts.add(record.key());
        }
        return alerts;
    }
}
