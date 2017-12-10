package com.example.demo;

import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Web {

    private static final Logger log = LoggerFactory.getLogger("tracker");

    private static final Tracer tracer = GlobalTracer.get();

    @Bean
    public Logger getLog() {
        return log;
    }

    @Bean
    public Tracer getTracer() {
        return tracer;
    }

	public static void main(String[] args) {
		SpringApplication.run(Web.class, args);
	}
}
