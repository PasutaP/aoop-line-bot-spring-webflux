package com.example.lineapibackend.flexMessages.footerblocks;

import com.example.lineapibackend.flexMessages.AnnotatedStrategyFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class FooterBlockStrategyFactory extends AnnotatedStrategyFactory<FooterBlockImplementation, FooterBlock> {
    @Override
    public Class<FooterBlockImplementation> strategyAnnotation() {
        return FooterBlockImplementation.class;
    }

    @Override
    public Class<FooterBlock> strategyInterface() {
        return FooterBlock.class;
    }
}
