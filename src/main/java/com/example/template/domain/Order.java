package com.example.template.domain;

import lombok.Data;
import org.springframework.data.annotation.Version;

import java.util.UUID;

@Data
public class Order {
    public Integer id;

    @Version
    public Integer version;

    public Integer orderNumber;
}
