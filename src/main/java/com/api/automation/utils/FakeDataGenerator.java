package com.api.automation.utils;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.util.Locale;

public class FakeDataGenerator {

    private static final String URL_PATTERN = "https://[a-z1-9]{10}\\.dummycom/photos/[1-9]{2}\\.jpg";
    static final FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-US"), new RandomService());
    static final Faker faker = new Faker();

    public static Long generateRandomNumber() {
        return faker.number().randomNumber(10, false);
    }

    public static String generateRandomAnimal() {
        return faker.animal().name();
    }

    public static String generateRandomPetName() {
        return faker.pokemon().name();
    }

    public static String generateRandomUrl() {
        return fakeValuesService.regexify(URL_PATTERN);
    }
}
