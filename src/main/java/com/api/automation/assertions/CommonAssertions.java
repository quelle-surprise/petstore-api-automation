package com.api.automation.assertions;

import com.api.automation.pojo.ApiResponse;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

public class CommonAssertions {

    public static void badInputAssertion(ApiResponse apiResponse) {
        assertThat(apiResponse.getCode()).isEqualTo(SC_BAD_REQUEST);
        assertThat(apiResponse.getType()).isEqualTo("unknown");
        assertThat(apiResponse.getMessage()).isEqualTo("bad input");
    }
}
