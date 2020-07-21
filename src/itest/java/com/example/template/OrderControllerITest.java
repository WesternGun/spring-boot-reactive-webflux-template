package com.example.template;

import com.example.template.services.KafkaService;
import com.example.template.services.MetricService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.main.web-application-type=reactive")
@AutoConfigureWebTestClient
@ActiveProfiles("itest")
public class OrderControllerITest {
    @Autowired
    private ITestClient testClient;

    @SpyBean
    private KafkaService kafkaService;

    @SpyBean
    private MetricService metricService;

    @Test
    void shouldCompleteHappyPath() {
        // given mocked(here thread "Test worker" has one interaction with the aspect, which is not desired)
        doNothing().when(kafkaService).send(ArgumentMatchers.any());

        // when(real request)
        testClient.create()
                .expectStatus().isOk(); // here thread "parallel-1" has another interraction with aspect, which is what we want

        // then
        verify(kafkaService).send(ArgumentMatchers.any());
        verify(metricService).incKafka(); // fail because there are two invocations with this mock

        /*
            2020-07-08 19:26:36.504  INFO 13980 --- [    Test worker] c.e.template.services.MetricService      : Kafka counter incremented!
            2020-07-08 19:26:36.505  INFO 13980 --- [    Test worker] c.e.template.services.MetricAspect       : Kafka event inc success! Event: null
            2020-07-08 19:26:36.767  INFO 13980 --- [     parallel-1] c.e.template.services.BusinessService    : Start processing order: Order(id=a09c349d-7ca9-4a01-a310-73b73af8c282, orderNumber=1)
            2020-07-08 19:26:36.776  INFO 13980 --- [     parallel-1] c.e.template.services.BusinessService    : Order -> Kafka event: KafkaEventMessage(id=a09c349d-7ca9-4a01-a310-73b73af8c282, orderNumber=1)
            2020-07-08 19:26:36.789  INFO 13980 --- [     parallel-1] c.e.template.services.MetricService      : Kafka counter incremented! 2020-07-08 19:26:36.789  INFO 13980 --- [     parallel-1] c.e.template.services.MetricAspect       : Kafka event inc success! Event: KafkaEventMessage(id=a09c349d-7ca9-4a01-a310-73b73af8c282, orderNumber=1)
            2020-07-08 19:26:36.790 TRACE 13980 --- [     parallel-1] c.e.template.services.BusinessService    : Kafka message sent! KafkaEventMessage(id=a09c349d-7ca9-4a01-a310-73b73af8c282, orderNumber=1)

            org.mockito.exceptions.verification.TooManyActualInvocations:
            metricService.incKafka();
            Wanted 1 time:
            -> at com.example.template.OrderControllerITest.shouldCompleteHappyPath(OrderControllerITest.java:45)
            But was 2 times:
            -> at com.example.template.services.MetricAspect.incKafkaCounter(MetricAspect.java:19)
            -> at com.example.template.services.MetricAspect.incKafkaCounter(MetricAspect.java:19)
         */
    }

}
