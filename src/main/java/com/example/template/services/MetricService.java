package com.example.template.services;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MetricService {

    private final Counter kafkaCounter;

    public MetricService(MeterRegistry meterRegistry, @Value("${spring.application.name}") String appName) {
        this.kafkaCounter = Counter.builder(appName + ".kafka.sent")
                .description("number of kafka message sent")
                .register(meterRegistry);
    }

    public void incKafka() {
        kafkaCounter.increment();
        log.info("Kafka counter incremented!");
    }
}
