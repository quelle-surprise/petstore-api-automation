package com.api.automation.helpers;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class RestAssuredHelper {

    public Response sendGet(RequestSpecification requestSpecification, String endpoint) {
        return given()
                .spec(requestSpecification)
                .when()
                .get(endpoint);
    }

    public Response sendPost(RequestSpecification requestSpecification, String endpoint, Object body) {
        return requestSpecificationWithBody(requestSpecification, body)
                .post(endpoint);
    }

    public Response sendPostWithoutBody(RequestSpecification requestSpecification, String endpoint) {
        return given()
                .spec(requestSpecification)
                .when()
                .post(endpoint);
    }

    public Response sendPut(RequestSpecification requestSpecification, String endpoint, Object body) {
        return requestSpecificationWithBody(requestSpecification, body)
                .put(endpoint);
    }

    public Response sendDelete(RequestSpecification requestSpecification, String endpoint) {
        return given()
                .spec(requestSpecification)
                .when()
                .delete(endpoint);
    }

    private RequestSpecification requestSpecificationWithBody(RequestSpecification requestSpecification, Object body) {
        return given()
                .spec(requestSpecification)
                .body(body);
    }
}
