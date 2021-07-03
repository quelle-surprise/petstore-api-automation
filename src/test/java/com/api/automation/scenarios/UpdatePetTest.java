package com.api.automation.scenarios;

import com.api.automation.builders.PetBuilder;
import com.api.automation.helpers.PetsRequestHelper;
import com.api.automation.pojo.ApiResponse;
import com.api.automation.pojo.Pet;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.api.automation.assertions.CommonAssertions.badInputAssertion;
import static com.api.automation.pojo.Status.available;
import static com.api.automation.pojo.Status.pending;
import static com.api.automation.pojo.Status.sold;
import static com.api.automation.utils.FakeDataGenerator.generateRandomPetName;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.with;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

public class UpdatePetTest extends BaseTest {

    @Autowired
    private PetsRequestHelper petsRequestHelper;

    @Test
    @Description("Test for updating an existing pet with name and status via PUT /pet endpoint")
    public void shouldUpdateExistingPet() {
        // precondition - create new pet
        final Pet requestBody = petsRequestHelper.createPetAndAssert(available);

        final Pet updatedBody = requestBody.toBuilder()
                .id(requestBody.getId())
                .name(generateRandomPetName())
                .status(sold)
                .build();
        response = petsRequestHelper.updatePet(updatedBody);

        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        petsRequestHelper.findByIdAndAssert(updatedBody);
    }

    @Test
    @Description("Test for updating existing pet data with form data")
    public void shouldUpdateExistingPetWithFormData() {
        // precondition - create new pet
        final Pet requestBody = petsRequestHelper.createPetAndAssert(available);
        final Pet updatedBody = requestBody.toBuilder().name(generateRandomPetName()).status(pending).build();

        with().pollInterval(fibonacci(MILLISECONDS)).await().atMost(30, SECONDS).untilAsserted(() -> {
            response = petsRequestHelper.updatePetWithFormData(requestBody.getId(), updatedBody);
            assertThat(response.getStatusCode()).isEqualTo(SC_OK);
            petsRequestHelper.findByIdAndAssert(updatedBody);
        });
    }

    @Test
    public void shouldCreateNewPetViaUpdateEndpoint() {
        final Pet requestBody = PetBuilder.petDataWithAllParams(available).build();
        response = petsRequestHelper.updatePet(requestBody);

        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        petsRequestHelper.findByIdAndAssert(requestBody);
    }


    @Test
    public void shouldUpdateImageForExistingPet() {
        final Pet requestBody = petsRequestHelper.createPetAndAssert(available);

        File file = new File("./src/test/resources/animal_image.jpg");

        ApiResponse apiResponse = petsRequestHelper.uploadPetImage(requestBody.getId(), "testMetadata", file).as(ApiResponse.class);

        assertThat(apiResponse.getCode()).isEqualTo(SC_OK);
        assertThat(apiResponse.getType()).isEqualTo("unknown");
        assertThat(apiResponse.getMessage()).contains("animal_image.jpg", "testMetadata");
    }

    @Test
    public void shouldReturn400ForIncorrectUpdateData() {
        ApiResponse apiResponse = petsRequestHelper.updatePet(INCORRECT_BODY_WITH_WRONG_ID).as(ApiResponse.class);

        badInputAssertion(apiResponse);
    }

}
