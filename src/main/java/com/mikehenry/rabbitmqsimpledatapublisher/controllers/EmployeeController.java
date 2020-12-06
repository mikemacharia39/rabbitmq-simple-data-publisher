package com.mikehenry.rabbitmqsimpledatapublisher.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikehenry.rabbitmqsimpledatapublisher.configuration.RabbitMQConfiguration;
import com.mikehenry.rabbitmqsimpledatapublisher.dto.EmployeeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    RabbitMQConfiguration rabbitMQConfiguration = new RabbitMQConfiguration();
    String queueName = "EMPLOYEE_DATA_CONSUMER";

    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @PostMapping("/create")
    public ResponseEntity<Object> createEmployee(@RequestBody EmployeeDTO request) {
        logger.info("Received request: " + request.toString());
        try {
            ObjectMapper requestObjectMapper = new ObjectMapper();
            Map<Object, Object> paramMap = new HashMap<>();
            paramMap.put("employeeName", request.getEmployeeName());
            paramMap.put("salary", request.getSalary());
            paramMap.put("age", request.getAge());

            String params = requestObjectMapper.writeValueAsString(paramMap);

            logger.info("Publishing request: " + params);

            boolean isSuccessPublish = rabbitMQConfiguration.publishMessage(params, queueName);
            if (!isSuccessPublish) {
                Map<Object, Object> response = new HashMap<>();
                response.put("statusCode", 0);
                response.put("message", "Your request to create employee was unsuccessful");

                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Map<Object, Object> response = new HashMap<>();
            response.put("statusCode", 1);
            response.put("message", "Your request to create employee was successful");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            logger.error("JSONProcessing error " + e.getMessage());
        }

        return new ResponseEntity<>(defaultError(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<Object, Object> defaultError() {
        Map<Object, Object> response = new HashMap<>();
        response.put("statusCode", 0);
        response.put("message", "Your request to create employee was unsuccessful");

        return response;
    }
}
