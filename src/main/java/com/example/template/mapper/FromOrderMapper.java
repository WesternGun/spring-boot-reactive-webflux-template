package com.example.template.mapper;

import com.example.template.domain.Order;
import com.example.template.pojo.external.KafkaEventMessage;
import org.springframework.stereotype.Component;

@Component
public class FromOrderMapper {
    public KafkaEventMessage toKafkaEventMessage(Order order) {
        return KafkaEventMessage.builder()
                .id(order.getId().toString())
                .orderNumber(order.getOrderNumber())
                .build();
    }
}
