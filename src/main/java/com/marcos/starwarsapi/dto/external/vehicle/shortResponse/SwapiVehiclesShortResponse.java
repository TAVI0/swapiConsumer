package com.marcos.starwarsapi.dto.external.vehicles.shortResponse;

import com.marcos.starwarsapi.dto.external.person.shortResponse.SwapiPersonShortResult;
import lombok.Data;

import java.util.List;

@Data
public class SwapiVehiclesShortResponse {
    private String message;
    private List<SwapiVehicleShortResult> results;
}
