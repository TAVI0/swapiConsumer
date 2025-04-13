package com.marcos.starwarsapi.dto.external.vehicles;

import lombok.Data;

import java.util.List;

@Data
public class SwapiVehiclesResponse {
    private String message;
    private List<SwapiVehicleResult> result;
}
