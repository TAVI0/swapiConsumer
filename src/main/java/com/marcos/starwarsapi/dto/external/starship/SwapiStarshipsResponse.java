package com.marcos.starwarsapi.dto.external.starship;

import lombok.Data;

import java.util.List;

@Data
public class SwapiStarshipsResponse {
    private String message;
    private List<SwapiStarshipResult> result;
}
