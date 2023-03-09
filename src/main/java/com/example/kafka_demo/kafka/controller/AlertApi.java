package com.example.kafka_demo.kafka.controller;

import com.example.kafka_demo.kafka.model.Alert;
import com.example.kafka_demo.kafka.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping()
    public List<Alert> fetchAlerts() {
        return alertService.getAlerts();
    }

}
