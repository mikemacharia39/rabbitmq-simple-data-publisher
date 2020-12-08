package com.mikehenry.rabbitmqsimpledatapublisher.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikehenry.rabbitmqsimpledatapublisher.configuration.RabbitConfig;
import com.mikehenry.rabbitmqsimpledatapublisher.configuration.RabbitMQConfiguration;
import com.mikehenry.rabbitmqsimpledatapublisher.dto.EmployeeDTO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {

    RabbitMQConfiguration rabbitMQConfiguration;
    RabbitConfig rabbitConfig;

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Object> createEmployee(@Valid @RequestBody EmployeeDTO request) {
        Logger logger = LoggerFactory.getLogger(EmployeeController.class);

        logger.info("Received request: " + request.toString());
        try {
            ObjectMapper requestObjectMapper = new ObjectMapper();
            Map<Object, Object> paramMap = new HashMap<>();
            paramMap.put("employeeName", request.getEmployeeName());
            paramMap.put("salary", new BigDecimal(request.getSalary()));
            paramMap.put("age", Integer.parseInt(request.getAge()));

            String params = requestObjectMapper.writeValueAsString(paramMap);

            logger.info("Publishing request: " + params);

            logger.info("Queue: " + rabbitConfig.getQueueName());

            boolean isSuccessPublish = rabbitMQConfiguration.publishMessage(
                    params,
                    rabbitConfig.getQueueName(),
                    rabbitConfig);

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
