package com.labs.cloud.function.pusub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.BackgroundFunction;
import com.google.cloud.functions.Context;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.pubsub.v1.PubsubMessage;
import com.labs.cloud.function.pusub.configuration.ConfigurationModule;
import com.labs.cloud.function.pusub.event.Event;
import com.labs.cloud.function.pusub.properties.PropertiesModule;

import java.nio.charset.StandardCharsets;

public class PubsubTriggerCloudFunction implements BackgroundFunction<PubsubMessage> {

    private final ObjectMapper objectMapper;

    public PubsubTriggerCloudFunction(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        Guice.createInjector(
                new ConfigurationModule(),
                new PropertiesModule()
        );
    }


    @Override
    public void accept(PubsubMessage pubsubMessage, Context context) throws Exception {

        String eventPayload = pubsubMessage.getData().toString(StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Event event = objectMapper.readValue(eventPayload, Event.class);



    }
}
