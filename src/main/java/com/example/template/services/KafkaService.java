package com.example.template.services;

import com.example.template.pojo.external.KafkaEventMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(KafkaEventMessage someEvent) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(
                    "topic_test",
                    String.valueOf(someEvent.getId()),
                    objectMapper.writeValueAsString(someEvent)
            );
            kafkaTemplate.send(record).get(2000, TimeUnit.MILLISECONDS);
            log.info("Kafka message sent: {}", record);
        } catch (JsonProcessingException e) {
            log.error("Cannot serializer message: {}", someEvent);
        } catch (InterruptedException e) {
            log.error("Interrupted!");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            log.error("Cannot retrieve!");
        } catch (TimeoutException e) {
            log.error("Timeout!");
        }

    }
}
