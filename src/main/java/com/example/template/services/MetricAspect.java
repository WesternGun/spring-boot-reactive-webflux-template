package com.example.template.services;

import com.example.template.pojo.external.KafkaEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class MetricAspect {
    private final MetricService metricService;
    @AfterReturning("execution(* com.example.template.services.KafkaService.send(..)) &&" +
            "args(event)")
    public void incKafkaCounter(KafkaEventMessage event) {
        metricService.incKafka();
        log.info("Kafka event inc success! Event: {}", event);
    }
}
