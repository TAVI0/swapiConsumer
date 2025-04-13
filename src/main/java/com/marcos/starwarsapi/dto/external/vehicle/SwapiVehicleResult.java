package com.marcos.starwarsapi.dto.external.vehicles;

import com.marcos.starwarsapi.dto.external.starship.SwapiStarshipProperties;
import lombok.Data;

@Data
public class SwapiVehicleResult {
    private SwapiVehicleProperties properties;
    private String uid;
    private String description;
}
