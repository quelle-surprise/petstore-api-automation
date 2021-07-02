package com.api.automation.builders;

import com.api.automation.pojo.Category;
import com.api.automation.pojo.Pet;
import com.api.automation.pojo.Status;
import com.api.automation.pojo.Tag;

import java.util.List;

public final class PetBuilder {

    public static Pet.PetBuilder petBasicRequestWithRequiredParams(int id, String name, List<String> photoUrls) {
        return Pet.builder()
                .id(id)
                .name(name)
                .photoUrls(photoUrls);
    }

    public static Pet.PetBuilder petRequestWithAllParams(int id, String name, List<String> photoUrls, Category category, Status status, List<Tag> tags) {
        return petBasicRequestWithRequiredParams(id, name, photoUrls)
                .category(category)
                .status(status)
                .tags(tags);
    }
}
