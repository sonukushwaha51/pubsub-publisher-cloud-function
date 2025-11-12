package com.labs.cloud.function.pusub.schedule;

import lombok.Data;

import java.time.temporal.ChronoUnit;

@Data
public class ScheduleConfig {

    private String eventId;

    private String offset;

    private ChronoUnit timeUnit;

}
