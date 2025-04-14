package com.marcos.starwarsapi.dto.external.vehicle;

import lombok.Data;

import java.util.List;

@Data
public class SwapiVehiclesResponse {
    private String message;
    private List<SwapiVehicleResult> result;
}
