package com.labs.cloud.function.pusub.schedule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SchedulingModule extends AbstractModule {

    @Override
    public void configure() {
        this.bind(SchedulingService.class).in(Singleton.class);
    }

    private final Map<String, ScheduleConfig> scheduleConfigMap = new LinkedHashMap<>();

    @Provides
    @Singleton
    public Map<String, ScheduleConfig> mapSchedulingConfig(ObjectMapper objectMapper) {
        TypeReference<List<ScheduleConfig>> typeReference = new TypeReference<List<ScheduleConfig>>() {
        };
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("scheduling-config.json")) {
            List<ScheduleConfig> scheduleConfigList = objectMapper.readValue(inputStream, typeReference);
            for (ScheduleConfig config : scheduleConfigList) {
                scheduleConfigMap.put(config.getEventId(), config);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  scheduleConfigMap;
    }
}
