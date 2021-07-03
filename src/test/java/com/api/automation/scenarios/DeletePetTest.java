package com.api.automation.scenarios;

import com.api.automation.helpers.PetsRequestHelper;
import com.api.automation.pojo.ApiResponse;
import com.api.automation.pojo.Pet;
import io.qameta.allure.Description;
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

public class DeletePetTest extends BaseTest {

    @Autowired
    private PetsRequestHelper petsRequestHelper;

    @Test
    @Description("Test for deletion of already existing pet")
    public void shouldDeleteExistingPet() {
        // Precondition
        final Pet requestBody = petsRequestHelper.createPetAndAssert(available);

        with().pollInterval(fibonacci(MILLISECONDS)).await().atMost(30, SECONDS).untilAsserted(() -> {
            response = petsRequestHelper.deletePetById(requestBody.getId());
            assertThat(response.getStatusCode()).isEqualTo(SC_OK);
            ApiResponse apiResponse = response.getBody().as(ApiResponse.class);
            assertThat(apiResponse.getCode()).isEqualTo(200);
            assertThat(apiResponse.getType()).isEqualTo("unknown");
            assertThat(apiResponse.getMessage()).isEqualTo(Long.toString(requestBody.getId()));
        });
    }

    @Test
    @Description("Test for check 404 response after executing deletion endpoint for not existing pet Id")
    public void shouldReturn404ForNotExistingPetWhileDeletion() {
        response = petsRequestHelper.deletePetById(-1);

        assertThat(response.getStatusCode()).isEqualTo(SC_NOT_FOUND);
    }
}
