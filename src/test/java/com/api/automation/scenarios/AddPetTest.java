package com.api.automation.scenarios;

import com.api.automation.helpers.PetsRequestHelper;
import com.api.automation.pojo.ApiResponse;
import com.api.automation.pojo.Pet;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.api.automation.assertions.CommonAssertions.badInputAssertion;
import static com.api.automation.builders.PetBuilder.petDataWithAllParams;
import static com.api.automation.builders.PetBuilder.petRequiredData;
import static com.api.automation.pojo.Status.available;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

public class AddPetTest extends BaseTest {

    @Autowired
    private PetsRequestHelper petsRequestHelper;


    private static Object[][] petCreationData() {
        return new Object[][] {
                {petRequiredData().build()},
                {petDataWithAllParams(available).build()}
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
    @Description("Test for adding new pet incorrect request body")
    public void shouldReturn400ForIncorrectAddData() {
        ApiResponse response = petsRequestHelper.addNewPet(INCORRECT_BODY_WITH_WRONG_ID).body().as(ApiResponse.class);

        badInputAssertion(response);
    }
}
