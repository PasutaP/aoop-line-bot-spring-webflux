package com.example.lineapibackend.flexMessages.bodyblocks;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface BodyBlockImplementation {
    String value() default "";
}

