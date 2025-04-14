package com.marcos.starwarsapi.dto.external.vehicle;

import lombok.Data;

@Data
public class SwapiVehicleResult {
    private SwapiVehicleProperties properties;
    private String uid;
    private String description;
}
