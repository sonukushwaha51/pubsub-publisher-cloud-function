package com.labs.cloud.function.pusub.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PubsubMessage {

    private String data;

    private Map<String, String> attributes;

}
