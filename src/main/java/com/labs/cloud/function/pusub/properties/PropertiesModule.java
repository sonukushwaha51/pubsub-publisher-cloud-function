package com.labs.cloud.function.pusub.properties;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesModule extends AbstractModule {

    @Override
    public void configure() {
        this.bind(PropertiesService.class).in(Singleton.class);
    }

    @Provides
    @Named("projectId")
    public String provideProjectId() {
        return System.getenv("GCP_PROJECT");
    }

    @Provides
    @Named("region")
    public String provideRegion() {
        return System.getenv("FUNCTION_REGION");
    }

    @Provides
    @Named("cloudTaskQueueName")
    public String getCloudTaskQueueName(Properties properties) {
        return properties.getProperty("cloudTaskQueueName");
    }

    @Provides
    @Named("cloudTaskServiceAccount")
    public String getCloudTaskServiceAccount() {
        return System.getenv("CLOUD_TASK_SERVICE_ACCOUNT");
    }

    @Provides
    @Singleton
    public Properties fetchProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException exception) {
            log.error("Error while reading from properties: {}", exception.getLocalizedMessage(), exception);
        }
        return properties;
    }

}
