package com.api.automation;

import com.api.automation.builders.PetBuilder;
import com.api.automation.helpers.PetsRequestHelper;
import com.api.automation.pojo.Category;
import com.api.automation.pojo.Pet;
import com.api.automation.pojo.Status;
import com.api.automation.pojo.Tag;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class PetTests extends BaseTest {

    @Autowired
    private PetsRequestHelper petsRequestHelper;

    @Test
    public void shouldCreateNewPet() {
        response = petsRequestHelper.addNewPet(PetBuilder.petRequestWithAllParams(
                10,
                "Joe_the_hedgehog",
                Collections.singletonList("URL"),
                Category.builder()
                        .id(1)
                        .name("hedgehog").build(),
                Status.AVAILABLE,
                Collections.singletonList(new Tag(1, "testTag"))).build());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        Pet extractedResponseBody = response.getBody().as(Pet.class);

    }

    @Test
    public void shouldUpdateExistingPet() {

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

}
