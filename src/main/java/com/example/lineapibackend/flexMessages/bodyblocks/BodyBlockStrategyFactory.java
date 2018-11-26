package com.example.lineapibackend.flexMessages.bodyblocks;


import com.example.lineapibackend.flexMessages.AnnotatedStrategyFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class BodyBlockStrategyFactory extends AnnotatedStrategyFactory<BodyBlockImplementation, BodyBlock> {
    @Override
    public Class<BodyBlockImplementation> strategyAnnotation() {
        return BodyBlockImplementation.class;
    }

    @Override
    public Class<BodyBlock> strategyInterface() {
        return BodyBlock.class;
    }
}
