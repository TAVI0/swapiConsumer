package com.marcos.starwarsapi.dto.external.vehicle;

import lombok.Data;

@Data
public class SwapiVehicleResponse {
    private String message;
    private SwapiVehicleResult result;
}
