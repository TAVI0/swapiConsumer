package com.marcos.starwarsapi.dto.external.person;

import lombok.Data;

@Data
public class SwapiPersonResult {
    private SwapiPersonProperties properties;
    private String uid;
    private String description;
}
