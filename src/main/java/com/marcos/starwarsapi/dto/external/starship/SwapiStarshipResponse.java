package com.marcos.starwarsapi.dto.external.starship;

import lombok.Data;

@Data
public class SwapiStarshipResponse {
    private String message;
    private SwapiStarshipResult result;
}
