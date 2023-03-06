package com.example.kafka_demo.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Greeting {

    private String msg;
    private String name;


    @Override
    public String toString() {
        return msg + ", " + name + "!";
    }
}