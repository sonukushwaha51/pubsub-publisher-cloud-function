package com.labs.cloud.function.pusub.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class ClassPathConfigurationService {

    @Inject
    public ClassPathConfigurationService() {

    }

    @Provides
    @Singleton
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
