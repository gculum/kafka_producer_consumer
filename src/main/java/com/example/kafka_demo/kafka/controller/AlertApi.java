package com.example.kafka_demo.kafka.controller;

import com.example.kafka_demo.kafka.model.Alert;
import com.example.kafka_demo.kafka.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/alert")
public class AlertApi {
    @Autowired
    private AlertService alertService;

    @PostMapping()
    public ResponseEntity<Alert> add(@RequestBody Alert alert) {
        return new ResponseEntity<>(alert, alertService.send(alert) ?
                HttpStatus.CREATED : HttpStatus.EXPECTATION_FAILED);
    }
/*
    @GetMapping()
    public List<Workout> findAll() {

        return workoutService.findWorkouts();
    }
*/
}
