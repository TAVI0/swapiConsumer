package com.marcos.starwarsapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcos.starwarsapi.dto.VehicleDTO;

import com.marcos.starwarsapi.dto.external.swapi.SwapiListResponse;
import com.marcos.starwarsapi.dto.external.swapi.SwapiResponse;
import com.marcos.starwarsapi.dto.external.swapi.SwapiResult;
import com.marcos.starwarsapi.dto.external.swapi.SwapiShortResponse;
import com.marcos.starwarsapi.dto.external.SwapiVehicleProperties;
import com.marcos.starwarsapi.service.utiles.CacheService;
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
    private ObjectMapper objectMapper;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private CacheService cacheService;
    private final RestTemplate restTemplate;
    private final String swapiBaseUrl;
    HttpHeaders headers;
    HttpEntity<String> entity;

    public VehiclesServiceImp(RestTemplate restTemplate, @Value("${swapi-url}") String swapiBaseUrl) {
        this.restTemplate = restTemplate;
        this.swapiBaseUrl = swapiBaseUrl+ "vehicles/";

        headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        entity = new HttpEntity<>(headers);
    }
    @Override
    public VehicleDTO getVehicleById(String id) {
        String cacheKey = "vehicle_" + id;
        VehicleDTO dataCached = cacheService.get(cacheKey, VehicleDTO.class);
        if (dataCached != null) {
            return dataCached;
        }

        String url = swapiBaseUrl + id;
        try {
            ResponseEntity<SwapiResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiResponse.class);
            SwapiResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                dataCached = mapToVehicleDTO(response.getResult());
                cacheService.put(cacheKey, dataCached);
                return dataCached;
            }
        } catch (Exception e) {
            log.error("Error al obtener vehiculo por id: " + id, e);
        }
        return null;
    }

    @Override
    public List<VehicleDTO> getVehicles(int page, int limit) {
        String url = swapiBaseUrl + "?page=" + page + "&limit=" + limit;
        try {
            ResponseEntity<SwapiShortResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiShortResponse.class);
            SwapiShortResponse response = responseEntity.getBody();
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
        String url = swapiBaseUrl + "?name=" + name;
        try {
            ResponseEntity<SwapiListResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiListResponse.class);
            SwapiListResponse response = responseEntity.getBody();
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

    private VehicleDTO mapToVehicleDTO(SwapiResult result){
        SwapiVehicleProperties prop = objectMapper.convertValue(result.getProperties(), SwapiVehicleProperties.class);
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
