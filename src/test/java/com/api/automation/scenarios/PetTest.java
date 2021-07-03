package com.api.automation.scenarios;

import com.api.automation.helpers.PetsRequestHelper;
import com.api.automation.pojo.ApiResponse;
import com.api.automation.pojo.Pet;
import com.api.automation.pojo.Status;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.api.automation.builders.PetBuilder.petDataWithAllParams;
import static com.api.automation.builders.PetBuilder.petRequiredData;
import static com.api.automation.pojo.Status.*;
import static com.api.automation.utils.FakeDataGenerator.generateRandomPetName;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.with;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

public class PetTest extends BaseTest {

    @Autowired
    private PetsRequestHelper petsRequestHelper;

    private static Object[][] petCreationData() {
        return new Object[][] {
                {petRequiredData().build()},
                {petDataWithAllParams(available).build()}
        };
    }

    private static Object[][] findByStatusData() {
        return new Object[][] {
                {available},
                {sold},
                {pending}
        };
    }

    @ParameterizedTest
    @MethodSource("petCreationData")
    @Description("Test for adding new pet to the store")
    public void shouldCreateNewPet(Pet requestBody) {
        response = petsRequestHelper.addNewPet(requestBody);

        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        assertThat(response.getBody().as(Pet.class)).isEqualTo(requestBody);
    }

    @Test
    @Description("Test for updating an existing pet with name and status")
    public void shouldUpdateExistingPet() {
        // precondition - create new pet
        final Pet requestBody = createPetAndAssert(available);

        final Pet updatedBody = requestBody.toBuilder()
                .id(requestBody.getId())
                .name(generateRandomPetName())
                .status(sold)
                .build();
        response = petsRequestHelper.updateExistingPet(updatedBody);

        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        findByIdAndAssert(updatedBody);
    }

    @ParameterizedTest
    @MethodSource("findByStatusData")
    @Description("Test for finding existing pet by status - available, pending and sold")
    public void shouldFindExistingPetByStatus(Status status) {
        // precondition - create pet with given status
        final Pet requestBody = createPetAndAssert(status);

        with().pollInterval(fibonacci(MILLISECONDS)).await().atMost(30, SECONDS).untilAsserted(() -> {
            response = petsRequestHelper.findPetsByGivenStatus(status);
            List<Pet> listOfPetsWithGivenStatus = response.body().jsonPath().getList("", Pet.class);
            boolean isPetObjectPresentInResponse = listOfPetsWithGivenStatus.stream().anyMatch(p -> p.getId().equals(requestBody.getId()));
            assertThat(isPetObjectPresentInResponse).isTrue();
            assertThat(requestBody).isEqualTo(listOfPetsWithGivenStatus.stream().filter(p -> p.getId().equals(requestBody.getId())).findFirst().get());
        });
    }

    @Test
    @Description("Test for finding existing pet by status - available, pending and sold")
    public void shouldFindExistingPetByProvidedId() {
        // precondition - create new pet
        final Pet requestBody = createPetAndAssert(available);

        findByIdAndAssert(requestBody);
    }

    @Test
    @Description("Test for finding existing pet by status - available, pending and sold")
    public void shouldUpdateExistingPetWithFormData() {
        // precondition - create new pet
        final Pet requestBody = createPetAndAssert(available);
        final Pet updatedBody = requestBody.toBuilder().name(generateRandomPetName()).status(pending).build();

        with().pollInterval(fibonacci(MILLISECONDS)).await().atMost(30, SECONDS).untilAsserted(() -> {
            response = petsRequestHelper.updatePetWithFormData(requestBody.getId(), updatedBody);
            assertThat(response.getStatusCode()).isEqualTo(SC_OK);
            findByIdAndAssert(updatedBody);
        });
    }

    @Test
    public void shouldDeleteExistingPet() {
        // Precondition
        final Pet requestBody = createPetAndAssert(available);
        // Delete endpoint is sometimes flaky - it throws 404, so awaitility is added as a little workaround
        with().pollInterval(fibonacci(MILLISECONDS)).await().atMost(15, SECONDS).untilAsserted(() -> {
            response = petsRequestHelper.deletePetById(requestBody.getId());
            assertThat(response.getStatusCode()).isEqualTo(SC_OK);
            ApiResponse apiResponse = response.getBody().as(ApiResponse.class);
            assertThat(apiResponse.getCode()).isEqualTo(200);
            assertThat(apiResponse.getType()).isEqualTo("unknown");
            assertThat(apiResponse.getMessage()).isEqualTo(Long.toString(requestBody.getId()));
        });
    }

    @Test
    public void shouldReturn415ForNotSupportedMediaType() {
        // For all endpoints
    }

    @Test
    public void shouldReturn400ForBadInput() {
        // POST, PUT etc.
    }

    @Test
    public void shouldReturn404IfPetNotFound() {

    }

    private void findByIdAndAssert(Pet petObject) {
        with().pollInterval(fibonacci(MILLISECONDS)).await().atMost(10, SECONDS).untilAsserted(() -> {
            response = petsRequestHelper.findPetById(petObject.getId());
            assertThat(response.getStatusCode()).isEqualTo(SC_OK);
            assertThat(response.getBody().as(Pet.class)).isEqualTo(petObject);
        });
    }

    private Pet createPetAndAssert(Status status) {
        final Pet requestBody = petDataWithAllParams(status).build();
        response = petsRequestHelper.addNewPet(requestBody);
        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        return requestBody;
    }

}
