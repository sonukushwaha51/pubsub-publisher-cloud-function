package com.labs.cloud.function.pusub;

import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.pubsub.v1.PubsubMessage;
import com.labs.cloud.function.pusub.configuration.ConfigurationModule;
import com.labs.cloud.function.pusub.properties.PropertiesModule;
import com.labs.cloud.function.pusub.schedule.SchedulingModule;
import com.labs.cloud.function.pusub.schedule.SchedulingService;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class PubsubTriggerCloudFunction implements BackgroundFunction<PubsubMessage> {

    private final Injector injector;

    public PubsubTriggerCloudFunction() {
        this.injector = Guice.createInjector(
                new ConfigurationModule(),
                new PropertiesModule(),
                new SchedulingModule()
        );
    }


    @Override
    public void accept(PubsubMessage pubsubMessage, Context context) throws Exception {

        String eventPayload = pubsubMessage.getData().toString(StandardCharsets.UTF_8);
        SchedulingService schedulingService = injector.getInstance(SchedulingService.class);
        log.info("Proceeding with creating cloud task for event: {}", eventPayload);
        schedulingService.scheduleAndCreateTask(eventPayload);



    }
}
