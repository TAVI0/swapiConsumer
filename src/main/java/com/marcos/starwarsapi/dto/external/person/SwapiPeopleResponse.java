package com.marcos.starwarsapi.dto.external.person;

import lombok.Data;

import java.util.List;

@Data
public class SwapiPeopleResponse {
    private String message;
    private List<SwapiPersonResult> result;
}