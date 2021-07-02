package com.api.automation.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    AVAILABLE("available"),
    PENDING("pending"),
    SOLD("sold");

    private final String statusValue;
}
