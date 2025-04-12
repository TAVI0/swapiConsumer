package com.marcos.starwarsapi.dto.external.starship.shortResponse;

import com.marcos.starwarsapi.dto.external.person.shortResponse.SwapiPersonShortResult;
import lombok.Data;

import java.util.List;

@Data
public class SwapiStarshipsShortResponse {
    private String message;
    private List<SwapiPersonShortResult> results;
}
