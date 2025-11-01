package com.labs.cloud.function.pusub.schedule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SchedulingService {

    private final ObjectMapper objectMapper;

    @Inject
    public SchedulingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Provides
    @Singleton
    public Map<String, ScheduleConfig> mapSchedulingConfig() {
        Map<String, ScheduleConfig> scheduleConfigMap = new LinkedHashMap<>();
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
