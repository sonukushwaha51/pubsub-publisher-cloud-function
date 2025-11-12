package com.labs.cloud.function.pusub.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.tasks.v2.*;
import com.google.common.net.MediaType;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class CloudTaskService {

    private final ObjectMapper objectMapper;

    private final String cloudTaskServiceAccount;

    private static final String SCHEDULE_RUNNER_NAME = "http-publisher-function";

    @Inject
    public CloudTaskService(ObjectMapper objectMapper, @Named("cloudTaskServiceAccount") String cloudTaskServiceAccount) {
        this.objectMapper = objectMapper;
        this.cloudTaskServiceAccount = cloudTaskServiceAccount;
    }

    public void createTask(String cloudTaskQueueName, String region, String projectId, Timestamp scheduleTimestamp, String eventPayload) {
       try (CloudTasksClient cloudTasksClient = CloudTasksClient.create()) {
           QueueName queueName = QueueName.of(projectId, region, cloudTaskQueueName);
           String cloudFunctionUrl = "https://"+ region + "-" + projectId + ".cloudfunctions.net/" + SCHEDULE_RUNNER_NAME ;
           OidcToken oidcToken = OidcToken.newBuilder().setServiceAccountEmail(cloudTaskServiceAccount).build();
           Task task = Task.newBuilder()
                   .setScheduleTime(scheduleTimestamp)
                   .setHttpRequest(HttpRequest.newBuilder()
                           .setBody(ByteString.copyFrom(objectMapper.writeValueAsBytes(eventPayload)))
                           .setUrl(cloudFunctionUrl)
                           .setOidcToken(oidcToken)
                           .setHttpMethod(HttpMethod.POST)
                           .putHeaders("Content-Type", MediaType.JSON_UTF_8.type())
                           .build())
                           .build();
           cloudTasksClient.createTask(queueName, task);
           log.info("Cloud task created");
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }
}
