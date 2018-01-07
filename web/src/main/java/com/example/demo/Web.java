package com.example.demo;

import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class Web {

    private static final Tracer tracer = GlobalTracer.get();

    @Bean
    public Tracer getTracer() {
        return tracer;
    }

	public static void main(String[] args) {
		SpringApplication.run(Web.class, args);
	}
}
