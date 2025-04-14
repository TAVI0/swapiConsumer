package com.marcos.starwarsapi.dto.external.film;

import lombok.Data;

@Data
public class SwapiFilmResponse {
    private String message;
    private SwapiFilmResult result;
}
