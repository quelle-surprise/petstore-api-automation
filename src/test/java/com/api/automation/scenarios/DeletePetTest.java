package com.api.automation.scenarios;

import com.api.automation.helpers.PetsRequestHelper;
import com.api.automation.pojo.ApiResponse;
import com.api.automation.pojo.Pet;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.api.automation.pojo.Status.available;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.with;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

@DisplayName("Tests for pets deletion")
public class DeletePetTest extends BaseTest {

    @Autowired
    private PetsRequestHelper petsRequestHelper;

    @Test
    @DisplayName("Pet deletion via /pet/{petId} endpoint")
    public void shouldDeleteExistingPet() {
        final Pet requestBody = petsRequestHelper.createPetAndAssert(available);

        petsRequestHelper.findByIdAndAssert(requestBody);

        assertThatPetWasSuccessfullyDeleted(requestBody, petsRequestHelper.deletePetById(requestBody.getId()));
    }


    @Test
    @DisplayName("Pet deletion via /pet/{petId} endpoint with not existing ID - 404 expected")
    public void shouldReturn404ForNotExistingPetWhileDeletion() {
        response = petsRequestHelper.deletePetById(-1);

        assertThat(response.getStatusCode()).isEqualTo(SC_NOT_FOUND);
    }

    private void assertThatPetWasSuccessfullyDeleted(Pet requestBody, Response response) {
        with().pollInterval(fibonacci(MILLISECONDS)).await().atMost(30, SECONDS).untilAsserted(() -> {
            assertThat(response.getStatusCode()).isEqualTo(SC_OK);
            ApiResponse apiResponse = response.getBody().as(ApiResponse.class);
            assertThat(apiResponse.getCode()).isEqualTo(200);
            assertThat(apiResponse.getType()).isEqualTo("unknown");
            assertThat(apiResponse.getMessage()).isEqualTo(Long.toString(requestBody.getId()));
        });
    }

}
