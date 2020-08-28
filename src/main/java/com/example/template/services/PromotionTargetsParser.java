package com.example.template.services;

import com.example.template.pojo.internal.TargetsValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionTargetsParser {
    private final CustomerServiceAdapter customerServiceAdapter;

    private Mono<Set<String>> parseTargets(String location) {
        try {
            log.trace("Reading CSV file at: {}", location);
            return Mono.just(Files.lines(Path.of(URI.create(location))).collect(Collectors.toSet()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Mono<TargetsValidationResponse> checkTargetMember(String location) {
        TargetsValidationResponse response = new TargetsValidationResponse();
        return Mono.just(location).flatMap(this::parseTargets)
                .doOnError(throwable -> response.setError(throwable.getMessage()))
                .flatMap(customerServiceAdapter::findAllAndMissing)
                .doOnNext(idAndFoundMap -> {
                    log.trace("Targets processing result: {}", idAndFoundMap);
                    Set<String> foundIds = idAndFoundMap.entrySet().stream()
                            .filter(Map.Entry::getValue)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet()
                            );
                    response.setValidTargets(foundIds);
                    response.setProcessed(foundIds.size());
                    response.setInvalidTargets(idAndFoundMap.entrySet().stream()
                            .filter(entry -> !entry.getValue())
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet())
                    );
                })
                .then(Mono.just(response));
    }
}
