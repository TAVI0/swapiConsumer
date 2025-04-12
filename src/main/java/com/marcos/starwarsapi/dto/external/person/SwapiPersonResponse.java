package com.marcos.starwarsapi.dto.external.person;


import lombok.Data;

@Data
public class SwapiPersonResponse {
    private String message;
    private SwapiPersonResult result;
}

