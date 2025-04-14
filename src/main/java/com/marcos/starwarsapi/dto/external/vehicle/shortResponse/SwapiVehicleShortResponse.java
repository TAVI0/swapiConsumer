package com.marcos.starwarsapi.dto.external.vehicle.shortResponse;

import lombok.Data;

import java.util.List;

@Data
public class SwapiVehicleShortResponse {
    private String message;
    private List<SwapiVehicleShortResult> results;
}
