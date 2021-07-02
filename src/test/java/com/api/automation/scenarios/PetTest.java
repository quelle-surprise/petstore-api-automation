package com.api.automation.scenarios;

import com.api.automation.helpers.PetsRequestHelper;
import com.api.automation.pojo.Pet;
import com.api.automation.pojo.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.api.automation.builders.PetBuilder.petDataWithAllParams;
import static com.api.automation.builders.PetBuilder.petRequiredData;
import static com.api.automation.utils.FakeDataGenerator.*;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

public class PetTest extends BaseTest {

    @Autowired
    private PetsRequestHelper petsRequestHelper;

    private static Object[][] petCreationData() {
        return new Object[][] {
                {petRequiredData().build()},
                {petDataWithAllParams(Status.available).build()}
        };
    }

    @ParameterizedTest
    @MethodSource("petCreationData")
    public void shouldCreateNewPet(Pet requestBody) {
        // when
        response = petsRequestHelper.addNewPet(requestBody);
        // then
        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        assertThat(response.getBody().as(Pet.class)).isEqualTo(requestBody);
    }

    @Test
    public void shouldUpdateExistingPet() {
        // precondition - create new pet
        final Pet requestBody = petDataWithAllParams(Status.pending).build();
        response = petsRequestHelper.addNewPet(requestBody);
        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        // when
        final Pet updatedBody = requestBody.toBuilder()
                .id(requestBody.getId())
                .name(generateRandomPetName())
                .status(Status.sold)
                .build();
        response = petsRequestHelper.updateExistingPet(updatedBody);
        // then
        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        findByIdAndAssert(updatedBody);
    }

    @Test
    public void shouldFindExistingPetByStatus() {
        // check 3 statuses - available, pending, sold
    }

    @Test
    public void shouldFindExistingPetByProvidedId() {

    }

    @Test
    public void shouldUpdateExistingPetWithFormData() {

    }

    @Test
    public void shouldDeleteExistingPet() {

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
        response = petsRequestHelper.findPetById(petObject.getId());
        assertThat(response.getStatusCode()).isEqualTo(SC_OK);
        assertThat(response.getBody().as(Pet.class)).isEqualTo(petObject);
    }
}
