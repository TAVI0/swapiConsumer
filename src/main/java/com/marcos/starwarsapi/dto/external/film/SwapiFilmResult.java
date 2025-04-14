package com.marcos.starwarsapi.dto.external.film;

import lombok.Data;

@Data
public class SwapiFilmResult {
    private SwapiFilmProperties properties;
    private String uid;
    private String description;
}
