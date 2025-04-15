package com.marcos.starwarsapi.dto.external.swapi;

import lombok.Data;

import java.util.List;

@Data
public class SwapiListResponse {
    private String message;
    private List<SwapiResult> result;
}
