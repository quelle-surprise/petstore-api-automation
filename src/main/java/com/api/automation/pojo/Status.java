package com.api.automation.pojo;

import lombok.extern.jackson.Jacksonized;


@Jacksonized
public enum Status {
    available,
    pending,
    sold;

}
