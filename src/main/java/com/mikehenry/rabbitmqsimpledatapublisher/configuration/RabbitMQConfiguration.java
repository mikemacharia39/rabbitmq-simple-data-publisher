package com.mikehenry.rabbitmqsimpledatapublisher.configuration;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitMQConfiguration {

    Connection connection;
    Channel channel;
    Logger logger = LoggerFactory.getLogger(RabbitMQConfiguration.class);

    public RabbitMQConfiguration() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setAutomaticRecoveryEnabled(true);

        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            logger.error("IOException occurred connecting to rabbitmq. Error: " + e.getMessage());
        } catch (TimeoutException e) {
            logger.error("TimeoutException occurred connecting to rabbitmq. Error: " + e.getMessage());
        }
    }

    /**
     * publish message to queue
     * @param message Message to publish to queue
     * @param queueName queue name
     * @return boolean true if published
     */
    public boolean publishMessage(String message, String queueName) {
        boolean success = false;
        try {
            channel.exchangeDeclare("amq.direct", "direct", true);

            Map<String, Object> queueArgs = new HashMap<>();
            queueArgs.put("x-queue-type", "classic");

            channel.queueDeclare(queueName, true, false, false, queueArgs);

            channel.queueBind(queueName, "amq.direct", queueName);

            channel.basicPublish("amq.direct", queueName, null, message.getBytes(StandardCharsets.UTF_8));

            logger.info("Successfully published message");

            success = true;
        } catch (ShutdownSignalException e) {
            logger.error("ShutdownSignalException occurred publishing message in rabbitmq. Error: " + e.getMessage());
        }catch (IOException e) {
            logger.error("IOException occurred publishing message in rabbitmq. Error: " + e.getMessage());
            e.printStackTrace();
        }

        return success;
    }

}
