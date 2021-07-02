package com.api.automation.helpers;

import com.api.automation.pojo.Pet;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PetsRequestHelper {

    @Value("${petstore-api.url}")
    private String petsUrl;

    @Value("${petstore-api.endpoints.pet}")
    private String petCreateOrUpdateEndpoint;

    private final RestAssuredHelper restAssuredHelper;

    public PetsRequestHelper(RestAssuredHelper restAssuredHelper) {
        this.restAssuredHelper = restAssuredHelper;
    }

    public Response uploadPetImage() {
        return null;
    }

    public Response addNewPet(Pet pet) {
        return restAssuredHelper.sendPost(petsRequestSpecification(), petCreateOrUpdateEndpoint, pet);
    }

    public Response updateExistingPet(Pet pet) {
        return null;
    }

    public Response findPetByGivenStatus() {
        return null;
    }

    public Response findPetById() {
        return null;
    }

    /*
        Allows update name and status of the given pet
     */
    public Response updatePetWithFormData() {
        return null;
    }

    public Response deletePetById(int petId) {
        return null;
    }

    private RequestSpecification petsRequestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(petsUrl)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }
}
