package com.maybeitssquid.web.model;

import io.opentracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Configuration
public class TourismService implements Tourism {

    @Autowired
    private final Tracer tracer;

    @Override
    public String info(Locale locale, String country, Optional<String> postal) {
        return "Anything can happen";
    }
}
