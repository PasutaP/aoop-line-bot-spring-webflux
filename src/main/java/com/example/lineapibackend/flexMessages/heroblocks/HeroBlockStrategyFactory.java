package com.example.lineapibackend.flexMessages.heroblocks;

import com.example.lineapibackend.flexMessages.AnnotatedStrategyFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class HeroBlockStrategyFactory extends AnnotatedStrategyFactory<HeroBlockImplementation, HeroBlock> {
    @Override
    public Class<HeroBlockImplementation> strategyAnnotation() {
        return HeroBlockImplementation.class;
    }

    @Override
    public Class<HeroBlock> strategyInterface() {
        return HeroBlock.class;
    }
}
