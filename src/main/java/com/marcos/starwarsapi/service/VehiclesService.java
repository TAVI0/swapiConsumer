package com.marcos.starwarsapi.service;

import com.marcos.starwarsapi.dto.VehicleDTO;

import java.util.List;

public interface VehiclesService {
    VehicleDTO getVehicleById(String id);
    List<VehicleDTO> getVehicles(int page, int limit);
    List<VehicleDTO> getVehiclesByName(String name);
}
