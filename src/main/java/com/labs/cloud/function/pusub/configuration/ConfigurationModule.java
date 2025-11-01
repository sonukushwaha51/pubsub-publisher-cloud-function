package com.labs.cloud.function.pusub.configuration;

import com.google.inject.AbstractModule;

public class ConfigurationModule extends AbstractModule {

    @Override
    public void configure() {
        this.bind(ConfigurationService.class).to(ClassPathConfigurationService.class);
    }
}
