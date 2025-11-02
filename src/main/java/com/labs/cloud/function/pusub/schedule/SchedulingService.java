package com.labs.cloud.function.pusub.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.protobuf.Timestamp;
import com.labs.cloud.function.pusub.event.Event;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SchedulingService {

    private static final Clock clock = Clock.system(ZoneId.of("Asia/Kolkata"));

    private final ObjectMapper objectMapper;

    private final String projectId;

    private final String region;

    private final String cloudTaskQueueName;

    private final CloudTaskService cloudTaskService;

    private final Map<String, ScheduleConfig> scheduleConfigMap = new LinkedHashMap<>();

    @Inject
    public SchedulingService(ObjectMapper objectMapper,
                             @Named("projectId") String projectId,
                             @Named("region") String region,
                             @Named("cloudTaskQueueName") String cloudTaskQueueName, CloudTaskService cloudTaskService) {
        this.objectMapper = objectMapper;
        this.projectId = projectId;
        this.region = region;
        this.cloudTaskQueueName = cloudTaskQueueName;
        this.cloudTaskService = cloudTaskService;
    }


    @Provides
    @Singleton
    public Map<String, ScheduleConfig> mapSchedulingConfig() {
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

    public ScheduleConfig getScheduleConfig(String eventId) {
        return scheduleConfigMap.get(eventId);
    }

    public void scheduleAndCreateTask(String eventPayload) {

        try {
            Event event = objectMapper.readValue(eventPayload, Event.class);
            ScheduleConfig scheduleConfig = getScheduleConfig(event.getEventId());
            if (scheduleConfig == null) {
                log.error("Invalid eventId: {}", event.getEventId());
                return;
            }

            Timestamp scheduleTimestamp = null;
            cloudTaskService.createTask(cloudTaskQueueName, region, projectId, scheduleTimestamp, eventPayload);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(exception);
        }

    }
}
