package com.labs.cloud.function.pusub.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.protobuf.Timestamp;
import com.labs.cloud.function.pusub.event.Event;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Slf4j
public class SchedulingService {

    private static final Clock clock = Clock.system(ZoneId.of("Asia/Kolkata"));

    private final ObjectMapper objectMapper;

    private final String projectId;

    private final String region;

    private final String cloudTaskQueueName;

    private final CloudTaskService cloudTaskService;

    private final Map<String, ScheduleConfig> scheduleConfigMap;

    @Inject
    public SchedulingService(ObjectMapper objectMapper,
                             @Named("projectId") String projectId,
                             @Named("region") String region,
                             @Named("cloudTaskQueueName") String cloudTaskQueueName, CloudTaskService cloudTaskService, Map<String, ScheduleConfig> scheduleConfigMap) {
        this.objectMapper = objectMapper;
        this.projectId = projectId;
        this.region = region;
        this.cloudTaskQueueName = cloudTaskQueueName;
        this.cloudTaskService = cloudTaskService;
        this.scheduleConfigMap = scheduleConfigMap;
    }

    public ScheduleConfig getScheduleConfig(String eventId) {
        return scheduleConfigMap.get(eventId);
    }

    public void scheduleAndCreateTask(Event event) {

        try {
            ScheduleConfig scheduleConfig = getScheduleConfig(event.getEventId());
            if (scheduleConfig == null) {
                log.error("Invalid eventId: {}", event.getEventId());
                return;
            }
            String eventPayload = objectMapper.writeValueAsString(event);
            Timestamp scheduleTimestamp = getScheduleTimestamp(scheduleConfig);
            cloudTaskService.createTask(cloudTaskQueueName, region, projectId, scheduleTimestamp, eventPayload);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(exception);
        }

    }

    public Timestamp getScheduleTimestamp(ScheduleConfig scheduleConfig) {
        ZonedDateTime zonedDateTime = clock.instant().atZone(clock.getZone());
        log.info("Current date : {}", zonedDateTime.toEpochSecond());
        String offset = scheduleConfig.getOffset() == null ? "0" : scheduleConfig.getOffset();
        ChronoUnit chronoUnit = scheduleConfig.getTimeUnit() == null ? ChronoUnit.DAYS : scheduleConfig.getTimeUnit();
        long offsetTime = Long.parseLong(offset) * chronoUnit.getDuration().toMillis();
        log.info("Offset time: {}", offsetTime);
        long scheduledTime = (zonedDateTime.toEpochSecond() + offsetTime) / 1000;
        log.info("Scheduling event for time: {}", ZonedDateTime.ofInstant(Instant.ofEpochSecond(scheduledTime), clock.getZone()));
        return Timestamp.newBuilder().setSeconds(scheduledTime).build();
    }

}
