package com.example.template.pojo.external;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KafkaEventMessage {
    private String id;
    private Integer orderNumber;

}
