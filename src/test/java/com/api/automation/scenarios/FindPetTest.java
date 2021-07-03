package com.api.automation.scenarios;

import com.api.automation.helpers.PetsRequestHelper;
import com.api.automation.pojo.Pet;
import com.api.automation.pojo.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.api.automation.pojo.Status.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.with;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;

@DisplayName("Tests for finding pets")
public class FindPetTest extends BaseTest {

    @Autowired
    private PetsRequestHelper petsRequestHelper;

    private static Object[][] findByStatusData() {
        return new Object[][] {
                {available},
                {sold},
                {pending}
        };
    }

    @ParameterizedTest(name = "Find pets with {0} status via /pet/findByStatus endpoint")
    @MethodSource("findByStatusData")
    @DisplayName("Finding pets with given status via /pet/findByStatus endpoint")
    public void shouldFindExistingPetByStatus(Status status) {
        final Pet requestBody = petsRequestHelper.createPetAndAssert(status);

        with().pollInterval(fibonacci(MILLISECONDS)).await().atMost(30, SECONDS).untilAsserted(() -> {
            response = petsRequestHelper.findPetsByGivenStatus(status);
            List<Pet> listOfPetsWithGivenStatus = response.body().jsonPath().getList("", Pet.class);
            boolean isPetObjectPresentInResponse = listOfPetsWithGivenStatus.stream().anyMatch(p -> p.getId().equals(requestBody.getId()));
            assertThat(isPetObjectPresentInResponse).isTrue();
            assertThat(requestBody).isEqualTo(listOfPetsWithGivenStatus.stream().filter(p -> p.getId().equals(requestBody.getId())).findFirst().get());
        });
    }

    @Test
    @DisplayName("Finding pet with provided ID via /pet/{petId} endpoint")
    public void shouldFindExistingPetByProvidedId() {
        final Pet requestBody = petsRequestHelper.createPetAndAssert(available);

        petsRequestHelper.findByIdAndAssert(requestBody);
    }

    @Test
    @DisplayName("Finding pet with provided non existing ID via /pet/{petId} endpoint - 404 response expected")
    public void shouldReturn404ForNotExistingPetWhileFinding() {
        response = petsRequestHelper.findPetById(-1);

        assertThat(response.getStatusCode()).isEqualTo(SC_NOT_FOUND);
    }
}
