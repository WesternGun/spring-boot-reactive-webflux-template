package com.example.template.domain;

import lombok.Data;
import org.springframework.data.annotation.Version;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "D_Order") // order is reserved word in H2
@Data
public class Order {
    @Id
    public Integer id;

    @Version
    public Integer version;

    public Integer orderNumber;
}
