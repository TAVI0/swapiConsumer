package com.marcos.starwarsapi.dto.external.swapi;

import lombok.Data;

@Data
public class SwapiResult{
    private Object properties;
    private String uid;
    private String description;
}
