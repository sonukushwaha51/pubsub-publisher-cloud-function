package com.labs.cloud.function.pusub.schedule;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class SchedulingModule extends AbstractModule {

    @Override
    public void configure() {
        this.bind(SchedulingService.class).in(Singleton.class);
    }
}
