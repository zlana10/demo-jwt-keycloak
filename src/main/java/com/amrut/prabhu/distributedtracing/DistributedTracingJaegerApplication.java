package com.amrut.prabhu.distributedtracing;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class DistributedTracingJaegerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributedTracingJaegerApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		MeterRegistry registry = new SimpleMeterRegistry();
		Counter counter = registry.counter("my.counter");
		counter.increment();
		return builder.
				build();
	}
}
