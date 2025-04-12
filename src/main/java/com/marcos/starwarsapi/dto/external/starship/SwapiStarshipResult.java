package com.marcos.starwarsapi.dto.external.starship;

import lombok.Data;

@Data
public class SwapiStarshipResult {
    private SwapiStarshipProperties properties;
    private String uid;
    private String description;
}
