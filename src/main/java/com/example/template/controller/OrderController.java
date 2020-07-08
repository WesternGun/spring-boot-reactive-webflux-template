package com.example.template.controller;

import com.example.template.domain.Order;
import com.example.template.services.BusinessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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


    @CrossOrigin
    @ApiOperation(value = "/create")
    @GetMapping(value = "/create", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> createOrder() {
        Order newOrder = new Order();
        newOrder.setId(UUID.randomUUID());
        newOrder.setOrderNumber(1);
        return businessService.processAndNotify(newOrder)
                .map(v -> ResponseEntity.status(200).body("success"));
    }

}
