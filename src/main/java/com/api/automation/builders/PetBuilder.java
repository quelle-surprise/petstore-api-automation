package com.api.automation.builders;

import com.api.automation.pojo.Category;
import com.api.automation.pojo.Pet;
import com.api.automation.pojo.Status;
import com.api.automation.pojo.Tag;

import java.util.Collections;

import static com.api.automation.utils.FakeDataGenerator.*;

public final class PetBuilder {

    public static Pet.PetBuilder petRequiredData() {
        return Pet.builder()
                .id(generateRandomNumber())
                .name(generateRandomPetName())
                .photoUrls(Collections.singletonList(generateRandomUrl()));
    }

    public static Pet.PetBuilder petDataWithAllParams(Status status) {
        return petRequiredData()
                .category(Category.builder()
                        .id(generateRandomNumber())
                        .name(generateRandomAnimal())
                        .build())
                .status(status)
                .tags(Collections.singletonList(Tag.builder().id(generateRandomNumber()).name(generateRandomPetName()).build()));
    }
}
