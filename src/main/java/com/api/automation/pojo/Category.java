package com.api.automation.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Category {

    private int id;
    private String name;
}
