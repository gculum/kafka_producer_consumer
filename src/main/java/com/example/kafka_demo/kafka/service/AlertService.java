package com.example.kafka_demo.kafka.service;

import com.example.kafka_demo.kafka.AlertCallback;
import com.example.kafka_demo.kafka.model.Alert;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    private Consumer<Alert, String> consumer;

    @PostConstruct
    public void init() {
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.example.kafka_demo.kafka.AlertLevelPartitioner");
        producerProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        producer = new KafkaProducer<Alert, String>(producerProps);


        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "helloconsumer");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.KEY_DEFAULT_TYPE, Alert.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new KafkaConsumer<Alert, String>(consumerProps);
        consumer.subscribe(Arrays.asList(alertTopic));
    }


    public boolean send(Alert alert) {
        ProducerRecord<Alert, String> producerRecord = new ProducerRecord<Alert, String>(alertTopic, alert, alert.getAlertMessage()); //
        //producer.send(producerRecord, new AlertCallback());

        try {
            RecordMetadata metadata = producer.send(producerRecord).get(2, TimeUnit.SECONDS);
            System.out.println("Added to partition " + metadata.partition());
            System.out.println("Offset " + metadata.offset());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            //TODO error handling
            return false;
        }
        return true;
    }


    public List<Alert> getAlerts() {

        List<Alert> alerts = new ArrayList<>();
        

        ConsumerRecords<Alert, String> records = consumer.poll(Duration.ofMillis(3000));
        for (ConsumerRecord<Alert, String> record : records) {
            alerts.add(record.key());
        }

        //consumer.close();
        return alerts;
    }


}
