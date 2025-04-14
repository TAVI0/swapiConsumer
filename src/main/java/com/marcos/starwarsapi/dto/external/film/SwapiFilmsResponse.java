package com.marcos.starwarsapi.dto.external.film;

import lombok.Data;

import java.util.List;

@Data
public class SwapiFilmsResponse {
    private String message;
    private List<SwapiFilmResult> result;
}
