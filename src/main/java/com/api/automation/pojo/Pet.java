package com.api.automation.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@Jacksonized
public class Pet {

    private Long id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Tag> tags;
    private Status status;

}
