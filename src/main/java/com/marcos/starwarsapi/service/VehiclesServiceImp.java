package com.marcos.starwarsapi.service;

import com.marcos.starwarsapi.dto.VehicleDTO;
import com.marcos.starwarsapi.dto.external.vehicle.SwapiVehicleProperties;
import com.marcos.starwarsapi.dto.external.vehicle.SwapiVehicleResponse;
import com.marcos.starwarsapi.dto.external.vehicle.SwapiVehicleResult;
import com.marcos.starwarsapi.dto.external.vehicle.SwapiVehiclesResponse;
import com.marcos.starwarsapi.dto.external.vehicle.shortResponse.SwapiVehicleShortResponse;
import com.marcos.starwarsapi.service.utiles.UtilsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VehiclesServiceImp implements VehiclesService{

    @Autowired
    private UtilsService utilsService;

    private final RestTemplate restTemplate;
    private final String swapiBaseUrl;
    HttpHeaders headers;
    HttpEntity<String> entity;

    public VehiclesServiceImp(RestTemplate restTemplate, @Value("${swapi-url}") String swapiBaseUrl) {
        this.restTemplate = restTemplate;
        this.swapiBaseUrl = swapiBaseUrl;

        headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        entity = new HttpEntity<>(headers);
    }
    @Override
    public VehicleDTO getVehicleById(String id) {
        String url = swapiBaseUrl + "vehicles/" + id;
        try {
            ResponseEntity<SwapiVehicleResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiVehicleResponse.class);
            SwapiVehicleResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return mapToVehicleDTO(response.getResult());
            }
        } catch (Exception e) {
            log.error("Error al obtener vehiculo por id: " + id, e);
        }
        return null;
    }

    @Override
    public List<VehicleDTO> getVehicles(int page, int limit) {
        String url = swapiBaseUrl + "vehicles/?page=" + page + "&limit=" + limit;
        try {
            ResponseEntity<SwapiVehicleShortResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiVehicleShortResponse.class);
            SwapiVehicleShortResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return response.getResults().stream()
                        .map(result -> getVehicleById(result.getUid()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error al obtener lista de vehiculos.", e);
        }
        return null;
    }

    @Override
    public List<VehicleDTO> getVehiclesByName(String name) {
        String url = swapiBaseUrl + "vehicles/?name=" + name;
        try {
            ResponseEntity<SwapiVehiclesResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiVehiclesResponse.class);
            SwapiVehiclesResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return response.getResult().stream()
                        .map(this::mapToVehicleDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error al obtener lista de vehiculos.", e);
        }
        return null;
    }

    private VehicleDTO mapToVehicleDTO(SwapiVehicleResult result){
        SwapiVehicleProperties prop = result.getProperties();
        VehicleDTO dto = new VehicleDTO();
        dto.setUid(result.getUid());
        dto.setName(prop.getName());
        dto.setModel(prop.getModel());
        dto.setVehicleClass(prop.getVehicle_class());
        dto.setManufacturer(prop.getManufacturer());
        dto.setCostInCredits(prop.getCost_in_credits());
        dto.setLength(prop.getLength());
        dto.setCrew(prop.getCrew());
        dto.setPassengers(prop.getPassengers());
        dto.setMaxAtmospheringSpeed(prop.getMax_atmosphering_speed());
        dto.setCargoCapacity(prop.getCargo_capacity());
        dto.setConsumables(prop.getConsumables());
        dto.setFilms(utilsService.transformUrls(prop.getFilms()));
        return dto;
    }
}
