package com.example.template.domain;

import lombok.Data;

import java.util.UUID;

@Data
public class Order {
    public UUID id;
    public Integer orderNumber;
}
