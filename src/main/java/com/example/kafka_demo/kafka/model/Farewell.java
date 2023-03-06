package com.example.kafka_demo.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Farewell {

    private String message;
    private Integer remainingMinutes;


    @Override
    public String toString() {
        return message + ". In " + remainingMinutes + "!";
    }

}