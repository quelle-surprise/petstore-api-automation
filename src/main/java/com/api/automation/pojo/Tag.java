package com.api.automation.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Tag {

    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;

}
