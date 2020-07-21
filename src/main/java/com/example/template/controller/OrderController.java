package com.example.template.controller;

import com.example.template.domain.Order;
import com.example.template.services.BusinessService;
import com.example.template.services.data.OrderService;
import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/order")
@Api(value = "Operations",
        tags = "Order controller")
@RequiredArgsConstructor
public class OrderController {
    private final BusinessService businessService;
    private final OrderService orderService;

    @CrossOrigin
    @ApiOperation(value = "/order/{id}", httpMethod = "POST")
    @PostMapping(value = "/order/{id}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Order>> createOrder(@PathVariable Integer id) {
        Order newOrder = new Order();
        newOrder.setId(id);
        newOrder.setOrderNumber(1);
        return businessService.processAndNotify(newOrder)
                .map(v -> ResponseEntity.status(201).body(newOrder));
    }

    @CrossOrigin
    @ApiOperation(value = "/order", httpMethod = "PUT")
    @PutMapping(value = "/order", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Order>> updateOrder(@RequestBody Order order) {
        return orderService.getAndUpdate(order)
                .map(updated -> ResponseEntity.status(201).body(updated));
    }

    @CrossOrigin
    @ApiOperation(value = "/order/{id}", httpMethod = "GET")
    @GetMapping(value = "/order/{id}")
    public Mono<ResponseEntity<Order>> getOrder(@PathVariable Integer id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.status(200).body(order));
    }

}
