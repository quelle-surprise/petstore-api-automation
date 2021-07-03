package com.api.automation.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Category {

    private long id;
    private String name;
}
