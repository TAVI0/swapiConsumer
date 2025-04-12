package com.marcos.starwarsapi.dto.external.person.shortResponse;

import lombok.Data;

import java.util.List;

@Data
public class SwapiPeopleShortResponse {
    private String message;
    private List<SwapiPersonShortResult> results;
}