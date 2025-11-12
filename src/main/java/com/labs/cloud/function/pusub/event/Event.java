package com.labs.cloud.function.pusub.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    private String eventId;

    private String eventUuid;

    private long timestamp;

    private Map<String, Object> fields;
}
