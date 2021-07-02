package com.api.automation.pojo;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
public class ApiResponse {

    private int code;
    private String type;
    private String message;

}
