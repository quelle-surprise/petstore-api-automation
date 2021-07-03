package com.api.automation.helpers;

import com.api.automation.pojo.Pet;
import com.api.automation.pojo.Status;
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

    @Value("${petstore-api.endpoints.find-by-status}")
    private String findyByStatusEndpoint;

    private final RestAssuredHelper restAssuredHelper;

    public PetsRequestHelper(RestAssuredHelper restAssuredHelper) {
        this.restAssuredHelper = restAssuredHelper;
    }

    public Response uploadPetImage() {
        return null;
    }

    public Response addNewPet(Pet pet) {
        return restAssuredHelper.sendPost(petsRequestSpecificationBuilder().setContentType(ContentType.JSON).build(), petCreateOrUpdateEndpoint, pet);
    }

    public Response updateExistingPet(Pet pet) {
        return restAssuredHelper.sendPut(petsRequestSpecificationBuilder().setContentType(ContentType.JSON).build(), petCreateOrUpdateEndpoint, pet);
    }

    public Response findPetsByGivenStatus(Status status) {
        return restAssuredHelper.sendGet(petsRequestSpecificationBuilder().addQueryParam("status", status).build(), findyByStatusEndpoint);
    }

    public Response findPetById(long id) {
        return restAssuredHelper.sendGet(petsRequestSpecificationBuilder().addPathParam("petId", id).build(), petByIdEndpoint);
    }

    /*
        Allows update name and status of the given pet
     */
    public Response updatePetWithFormData(long petId, Pet petObject) {
        return restAssuredHelper.sendPostWithoutBody(petsRequestSpecificationBuilder()
                .addPathParam("petId", petId)
                .addQueryParam("name", petObject.getName())
                .addQueryParam("status", petObject.getStatus()).build(), petByIdEndpoint);
    }

    public Response deletePetById(long petId) {
        return restAssuredHelper.sendDelete(petsRequestSpecificationBuilder().addPathParam("petId", petId).build(), petByIdEndpoint);
    }

    private RequestSpecBuilder petsRequestSpecificationBuilder() {
        return new RequestSpecBuilder()
                .setBaseUri(petsUrl)
                .log(LogDetail.ALL);
    }
}
