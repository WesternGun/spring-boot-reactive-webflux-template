package com.example.template.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CustomerServiceAdapter {

    /**
     * Naive impl for simplicity. Find all members with id and if found, return entry(id, true),
     * else return entry (id, false)
     * @param targetIds
     * @return
     */
    public Mono<Map<String, Boolean>> findAllAndMissing(@NotEmpty Set<String> targetIds) {
        return Mono.just(Map.of(
                "id1", true,
                "id2", false,
                "id3", true
        ));
    }
}
