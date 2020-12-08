package com.mikehenry.rabbitmqsimpledatapublisher.configuration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitMQConfiguration {

    Channel channel;

    public RabbitMQConfiguration(RabbitConfig rabbitConfig) {
        Logger logger = LoggerFactory.getLogger(RabbitMQConfiguration.class);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitConfig.getHost());
        connectionFactory.setPort(rabbitConfig.getPort());
        connectionFactory.setUsername(rabbitConfig.getUsername());
        connectionFactory.setPassword(rabbitConfig.getPassword());
        connectionFactory.setVirtualHost(rabbitConfig.getVhost());
        connectionFactory.setAutomaticRecoveryEnabled(true);

        try {
            Connection connection = connectionFactory.newConnection();
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
    public boolean publishMessage(String message, String queueName, RabbitConfig rabbitConfig) {
        Logger logger = LoggerFactory.getLogger(RabbitMQConfiguration.class);

        boolean success = false;
        try {
            // Exchange name
            // Exchange type
            // Durable
            channel.exchangeDeclare(rabbitConfig.getExchangeName(), rabbitConfig.getExchangeType(), true);

            // It is important to note a queues propertiesName
            // Durable (the queue will survive a broker restart)
            // Exclusive (used by only one connection and the queue will be deleted when that connection closes)
            // Auto-delete (queue that has had at least one consumer is deleted when last consumer unsubscribes)
            // Arguments (optional; used by plugins and broker-specific features such as message TTL, queue length limit etc)
            Map<String, Object> queueArgs = new HashMap<>();
            queueArgs.put("x-queue-type", "classic");
            channel.queueDeclare(queueName, true, false, false, queueArgs);

            // QueueName
            // Exchange name
            // routing key
            channel.queueBind(queueName, rabbitConfig.getExchangeName(), queueName);

            // Exchange name
            // Queue name
            // BasicProperties
            // Message in bytes
            channel.basicPublish(rabbitConfig.getExchangeName(), queueName, null,
                    message.getBytes(StandardCharsets.UTF_8));

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
