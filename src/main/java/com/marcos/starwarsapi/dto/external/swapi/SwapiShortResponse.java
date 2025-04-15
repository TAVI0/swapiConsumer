package com.marcos.starwarsapi.dto.external.swapi;

import lombok.Data;

import java.util.List;

@Data
public class SwapiShortResponse {
    private String message;
    private List<SwapiShortResult> results;
}