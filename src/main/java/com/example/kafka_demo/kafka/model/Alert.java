package com.example.kafka_demo.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Alert {

    private int alertId;
    private String stageId;
    private String alertLevel;
    private String alertMessage;

}