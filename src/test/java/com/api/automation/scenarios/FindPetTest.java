package com.api.automation.scenarios;

import com.api.automation.helpers.PetsRequestHelper;
import com.api.automation.pojo.Pet;
import com.api.automation.pojo.Status;
import io.qameta.allure.Description;
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

    @ParameterizedTest
    @MethodSource("findByStatusData")
    @Description("Test for finding existing pet by status - available, pending and sold")
    public void shouldFindExistingPetByStatus(Status status) {
        // precondition - create pet with given status
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
    @Description("Test for finding existing pet by provided Id")
    public void shouldFindExistingPetByProvidedId() {
        // precondition - create new pet
        final Pet requestBody = petsRequestHelper.createPetAndAssert(available);

        petsRequestHelper.findByIdAndAssert(requestBody);
    }

    @Test
    @Description("Test for check 404 response when given pet not exists")
    public void shouldReturn404ForNotExistingPetWhileFinding() {
        response = petsRequestHelper.findPetById(-1);

        assertThat(response.getStatusCode()).isEqualTo(SC_NOT_FOUND);
    }
}
