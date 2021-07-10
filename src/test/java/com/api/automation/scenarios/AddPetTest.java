package com.api.automation.scenarios;

import com.api.automation.builders.PetBuilder;
import com.api.automation.helpers.PetsRequestHelper;
import com.api.automation.pojo.ApiResponse;
import com.api.automation.pojo.Pet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.api.automation.assertions.CommonAssertions.badInputAssertion;
import static com.api.automation.builders.PetBuilder.petDataWithAllParams;
import static com.api.automation.builders.PetBuilder.petRequiredData;
import static com.api.automation.pojo.Status.available;
import static com.api.automation.utils.TestUtils.INCORRECT_BODY_WITH_WRONG_ID;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests for adding new pets")
public class AddPetTest extends BaseTest {

    @Autowired
    private PetsRequestHelper petsRequestHelper;


    private static Object[][] petCreationData() {
        return new Object[][] {
                {petRequiredData().build()},
                {petDataWithAllParams(available).build()}
        };
    }

    @ParameterizedTest(name = "Creating pet with given body = {0}")
    @MethodSource("petCreationData")
    @DisplayName("Creating new Pet via POST to /pet endpoint")
    public void shouldCreateNewPet(Pet requestBody) {
        response = petsRequestHelper.addNewPet(requestBody);

        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        assertThat(response.getBody().as(Pet.class)).isEqualTo(requestBody);
    }

    @Test
    @DisplayName("Creating new pet via PUT request to /pet endpoint")
    public void shouldCreateNewPetViaUpdateEndpoint() {
        final Pet requestBody = PetBuilder.petDataWithAllParams(available).build();
        response = petsRequestHelper.updatePet(requestBody);

        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        petsRequestHelper.findByIdAndAssert(requestBody);
    }

    @Test
    @DisplayName("Creating new Pet via POST to /pet endpoint with incorrect data - expected 400 response")
    public void shouldReturn400ForIncorrectAddData() {
        ApiResponse response = petsRequestHelper.addNewPet(INCORRECT_BODY_WITH_WRONG_ID).body().as(ApiResponse.class);

        badInputAssertion(response);
    }
}
