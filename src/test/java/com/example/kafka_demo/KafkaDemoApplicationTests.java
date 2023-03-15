package com.example.kafka_demo;

import com.example.kafka_demo.kafka.AlertLevelPartitioner;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class KafkaDemoApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("_X_ " + AlertLevelPartitioner.class.getName());
    }

}
