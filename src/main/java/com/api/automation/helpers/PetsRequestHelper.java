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

    @Value("${petstore-api.endpoints.pet-by-id}")
    private String petByIdEndpoint;

    private final RestAssuredHelper restAssuredHelper;

    public PetsRequestHelper(RestAssuredHelper restAssuredHelper) {
        this.restAssuredHelper = restAssuredHelper;
    }

    public Response uploadPetImage() {
        return null;
    }

    public Response addNewPet(Pet pet) {
        return restAssuredHelper.sendPost(petsRequestSpecificationBuilder().build(), petCreateOrUpdateEndpoint, pet);
    }

    public Response updateExistingPet(Pet pet) {
        return restAssuredHelper.sendPut(petsRequestSpecificationBuilder().build(), petCreateOrUpdateEndpoint, pet);
    }

    public Response findPetByGivenStatus() {
        return null;
    }

    public Response findPetById(int id) {
        return restAssuredHelper.sendGet(petsRequestSpecificationBuilder().addPathParam("petId", id).build(), petByIdEndpoint);
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

    private RequestSpecBuilder petsRequestSpecificationBuilder() {
        return new RequestSpecBuilder()
                .setBaseUri(petsUrl)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL);
    }
}
