package com.api.automation.scenarios;

import com.api.automation.helpers.PetsRequestHelper;
import com.api.automation.pojo.ApiResponse;
import com.api.automation.pojo.Pet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

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

@DisplayName("Tests for updating pets")
public class UpdatePetTest extends BaseTest {

    @Autowired
    private PetsRequestHelper petsRequestHelper;

    @Test
    @DisplayName("Updating currently existing pet via /pet endpoint")
    public void shouldUpdateExistingPet() {
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
    @DisplayName("Updating currently existing pet via /pet/{petId} endpoint")
    public void shouldUpdateExistingPetWithFormData() {
        final Pet requestBody = petsRequestHelper.createPetAndAssert(available);
        final Pet updatedBody = requestBody.toBuilder().name(generateRandomPetName()).status(pending).build();

        with().pollInterval(fibonacci(MILLISECONDS)).await().atMost(30, SECONDS).untilAsserted(() -> {
            response = petsRequestHelper.updatePetWithFormData(requestBody.getId(), updatedBody);
            assertThat(response.getStatusCode()).isEqualTo(SC_OK);
            petsRequestHelper.findByIdAndAssert(updatedBody);
        });
    }


    @Test
    @DisplayName("Updating image for existing pet via /pet/{petId}/uploadImage")
    public void shouldUpdateImageForExistingPet() {
        final Pet requestBody = petsRequestHelper.createPetAndAssert(available);

        File file = new File("./src/test/resources/animal_image.jpg");

        ApiResponse apiResponse = petsRequestHelper.uploadPetImage(requestBody.getId(), "testMetadata", file).as(ApiResponse.class);

        assertThat(apiResponse.getCode()).isEqualTo(SC_OK);
        assertThat(apiResponse.getType()).isEqualTo("unknown");
        assertThat(apiResponse.getMessage()).contains("animal_image.jpg", "testMetadata");
    }

    @Test
    @DisplayName("Updating pet with incorrect update data")
    public void shouldReturn400ForIncorrectUpdateData() {
        ApiResponse apiResponse = petsRequestHelper.updatePet(INCORRECT_BODY_WITH_WRONG_ID).as(ApiResponse.class);

        badInputAssertion(apiResponse);
    }

}
