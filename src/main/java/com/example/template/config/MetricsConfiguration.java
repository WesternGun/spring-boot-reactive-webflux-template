package com.example.template.config;

import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivefeign.client.metrics.MetricsTag;
import reactivefeign.client.metrics.MicrometerReactiveLogger;

import java.time.Clock;

@Configuration
public class MetricsConfiguration {

    private static final String APP_NAME = "template-project";

    @Bean
    public MeterFilter meterFilter(){
        return MeterFilter.denyNameStartsWith(("jvm"));
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    @Qualifier("metricsReactiveLogger")
    public MicrometerReactiveLogger metricsReactiveLogger(Clock clock,
                                                          PrometheusMeterRegistry meterRegistry) {
        return new MicrometerReactiveLogger(
                clock,
                meterRegistry,
                APP_NAME + ".feign.client_metrics",
                MetricsTag.getMandatory()
        );
    }


}
