package com.labs.cloud.function.pusub.schedule;

import lombok.Data;

@Data
public class ScheduleConfig {

    private String eventId;

    private String offset;

    private String timeUnit;

}
