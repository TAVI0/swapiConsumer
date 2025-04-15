package com.marcos.starwarsapi.dto.external.swapi;

import lombok.Data;

@Data
public class SwapiResponse {
    private String message;
    private SwapiResult result;
}
