package com.example.lineapibackend.flexMessages;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;

public abstract class AnnotatedStrategyFactory<A extends Annotation, T> {

    @Autowired
    private ListableBeanFactory beanFactory;

    public T getImplementation(String strategy) throws IllegalAccessException {
        Class<A> annotationClass = strategyAnnotation();
        Class<T> interfaceClass = strategyInterface();

        String[] names = beanFactory.getBeanNamesForAnnotation(annotationClass);

        for (String strategyName : names) {
            if (strategyName.equals(strategy)) {
                return beanFactory.getBean(strategyName, interfaceClass);
            }
        }

        throw new IllegalAccessException("There is no \"" + strategy + "\" strategy available for " + interfaceClass.getName());
    }

    public abstract Class<A> strategyAnnotation();

    public abstract Class<T> strategyInterface();
}
