package com.example.template.services.data;

import com.example.template.domain.Order;
import com.example.template.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Mono<Order> getOrderById(Integer id) {
        return Mono.just(orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found!")))
                .doOnError(e -> {
                    log.error("Cannot find order by id {}", id, e);
                });
    }

    public Mono<Order> getAndUpdate(Order newOrder) {
        return Mono.just(orderRepository.findById(newOrder.getId())
                .orElseThrow(() -> new RuntimeException("Not Found!"))
        ).map(found -> orderRepository.save(newOrder));
    }
}
