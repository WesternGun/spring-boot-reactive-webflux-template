package com.example.template.services;

import com.example.template.domain.Order;
import com.example.template.mapper.FromOrderMapper;
import com.example.template.pojo.external.KafkaEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessService {
    private final KafkaService kafkaService;
    private final FromOrderMapper fromOrderMapper;

    public Mono<Void> processAndNotify(Order order) {
        log.info("Start processing order: {}", order);

        return Mono.fromCallable(() -> fromOrderMapper.toKafkaEventMessage(order))
                .doOnSuccess(message -> log.info("Order -> Kafka event: {}",message))
                .doOnNext(message -> {
                    kafkaService.send(message);
                    log.trace("Kafka message sent! {}", message);
                })
                .then();
    }
}
