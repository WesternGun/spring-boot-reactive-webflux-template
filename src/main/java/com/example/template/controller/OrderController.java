package com.example.template.controller;

import com.example.template.domain.Order;
import com.example.template.pojo.internal.TargetsValidationResponse;
import com.example.template.services.BusinessService;
import com.example.template.services.PromotionTargetFileHandler;
import com.example.template.services.PromotionTargetsParser;
import com.example.template.services.data.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Api(value = "Operations",
        tags = "Order controller")
public class OrderController {
    private final BusinessService businessService;
    private final OrderService orderService;
    private final PromotionTargetFileHandler handler;
    private final PromotionTargetsParser parser;


    @CrossOrigin
    @ApiOperation(value = "/{id}", httpMethod = "POST")
    @PostMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Order>> createOrder(@PathVariable Integer id) {
        Order newOrder = new Order();
        newOrder.setId(id);
        newOrder.setOrderNumber(1);
        return businessService.processAndNotify(newOrder)
                .map(v -> ResponseEntity.status(201).body(newOrder));
    }

    @CrossOrigin
    @ApiOperation(value = "", httpMethod = "PUT")
    @PutMapping(value = "", produces = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Order>> updateOrder(@RequestBody Order order) {
        return orderService.getAndUpdate(order)
                .map(updated -> ResponseEntity.status(201).body(updated));
    }

    @CrossOrigin
    @ApiOperation(value = "/{id}", httpMethod = "GET")
    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<Order>> getOrder(@PathVariable Integer id) {
        return orderService.getOrderById(id)
                .map(order -> ResponseEntity.status(200).body(order));
    }

    @ApiOperation(
            value = "Upload target for drafting"
    )
    @PostMapping(value = "/{id}/draft/target", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Mono<TargetsValidationResponse> uploadTargets(@PathVariable("id") ObjectId id, @RequestPart Mono<FilePart> file) {
        return file.flatMap(handler::saveParts)
                .flatMap(parser::checkTargetMember);
    }

}
