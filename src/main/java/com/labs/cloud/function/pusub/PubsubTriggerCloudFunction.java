package com.labs.cloud.function.pusub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.labs.cloud.function.pusub.configuration.ConfigurationModule;
import com.labs.cloud.function.pusub.event.Event;
import com.labs.cloud.function.pusub.event.PubsubMessage;
import com.labs.cloud.function.pusub.properties.PropertiesModule;
import com.labs.cloud.function.pusub.schedule.SchedulingModule;
import com.labs.cloud.function.pusub.schedule.SchedulingService;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class PubsubTriggerCloudFunction implements BackgroundFunction<PubsubMessage> {

    private final Injector injector;

    private final ObjectMapper objectMapper;

    public PubsubTriggerCloudFunction() {
        this.injector = Guice.createInjector(
                new ConfigurationModule(),
                new PropertiesModule(),
                new SchedulingModule()
        );
        this.objectMapper = injector.getInstance(ObjectMapper.class);
    }


    @Override
    public void accept(PubsubMessage pubsubMessage, Context context) throws Exception {

        String eventPayload = pubsubMessage.getData();
        Event event = objectMapper.readValue(eventPayload, Event.class);
        SchedulingService schedulingService = injector.getInstance(SchedulingService.class);
        log.info("Proceeding with creating cloud task for event: {}", eventPayload);
        schedulingService.scheduleAndCreateTask(event);
    }
}
