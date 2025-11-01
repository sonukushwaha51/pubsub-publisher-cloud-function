package com.labs.cloud.function.pusub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.pubsub.v1.PubsubMessage;
import com.labs.cloud.function.pusub.configuration.ConfigurationModule;
import com.labs.cloud.function.pusub.event.Event;

import java.nio.charset.StandardCharsets;

public class PubsubTriggerCloudFunction implements BackgroundFunction<PubsubMessage> {

    public PubsubTriggerCloudFunction() {
        Guice.createInjector(
                new ConfigurationModule()
        );
    }


    @Override
    public void accept(PubsubMessage pubsubMessage, Context context) throws Exception {

        String eventPayload = pubsubMessage.getData().toString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Event event = objectMapper.readValue(eventPayload, Event.class);



    }
}
