package com.labs.cloud.function.pusub.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Scope;
import com.google.inject.Singleton;

public class ConfigurationModule extends AbstractModule {

    @Override
    public void configure() {
        this.bind(ClassPathConfigurationService.class).in(Singleton.class);
    }
}
