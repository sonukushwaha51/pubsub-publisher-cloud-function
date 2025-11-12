package com.labs.cloud.function.pusub.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ConfigurationModule extends AbstractModule {

    @Override
    public void configure() {
        this.bind(ClassPathConfigurationService.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
