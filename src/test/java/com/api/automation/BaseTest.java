package com.api.automation;

import com.api.automation.config.SpringConfig;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ContextConfiguration(classes = SpringConfig.class)
@TestPropertySource(properties = {"spring.config.location=classpath:configuration.yml"})
public class BaseTest {

    public Response response;
}
