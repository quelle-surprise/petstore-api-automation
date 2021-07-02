package com.api.automation.pojo;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class Response {

    private int code;
    private String type;
    private String message;

}
