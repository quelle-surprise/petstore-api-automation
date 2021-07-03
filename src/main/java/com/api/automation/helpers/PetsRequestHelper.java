package com.api.automation.helpers;

import com.api.automation.pojo.Pet;
import com.api.automation.pojo.Status;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.api.automation.builders.PetBuilder.petDataWithAllParams;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.with;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

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

    @Value("${petstore-api.endpoints.pet-image-upload}")
    private String petImageUploadEndpoint;

    private final RestAssuredHelper restAssuredHelper;

    public PetsRequestHelper(RestAssuredHelper restAssuredHelper) {
        this.restAssuredHelper = restAssuredHelper;
    }

    public Response uploadPetImage(long petId, String additionalMetadata, File file) {
        return restAssuredHelper.sendPostWithoutBody(petsRequestSpecificationBuilder()
                .addPathParam("petId", petId)
                .addParam("additionalMetadata", additionalMetadata)
                .addMultiPart(file)
                .build(), petImageUploadEndpoint);
    }

    public Response addNewPet(Object pet) {
        return restAssuredHelper.sendPost(petsRequestSpecificationBuilder().setContentType(ContentType.JSON).build(), petCreateOrUpdateEndpoint, pet);
    }

    public Response updatePet(Object pet) {
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

    public void findByIdAndAssert(Pet petObject) {
        with().pollInterval(fibonacci(MILLISECONDS)).await().atMost(10, SECONDS).untilAsserted(() -> {
            Response response = findPetById(petObject.getId());
            assertThat(response.getStatusCode()).isEqualTo(SC_OK);
            assertThat(response.getBody().as(Pet.class)).isEqualTo(petObject);
        });
    }

    public Pet createPetAndAssert(Status status) {
        final Pet requestBody = petDataWithAllParams(status).build();
        Response response = addNewPet(requestBody);
        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        return requestBody;
    }
}
