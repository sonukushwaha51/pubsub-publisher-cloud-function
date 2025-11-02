package com.labs.cloud.function.pusub.properties;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class PropertiesModule extends AbstractModule {

    @Override
    public void configure() {
        this.bind(PropertiesService.class).in(Singleton.class);
    }

}
