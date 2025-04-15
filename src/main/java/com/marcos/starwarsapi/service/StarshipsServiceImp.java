package com.marcos.starwarsapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcos.starwarsapi.dto.StarshipDTO;
import com.marcos.starwarsapi.dto.external.SwapiStarshipProperties;
import com.marcos.starwarsapi.dto.external.swapi.SwapiListResponse;
import com.marcos.starwarsapi.dto.external.swapi.SwapiResponse;
import com.marcos.starwarsapi.dto.external.swapi.SwapiResult;
import com.marcos.starwarsapi.dto.external.swapi.SwapiShortResponse;
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
public class StarshipsServiceImp implements StarshipsService{
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

    public StarshipsServiceImp(RestTemplate restTemplate, @Value("${swapi-url}") String swapiBaseUrl) {
        this.restTemplate = restTemplate;
        this.swapiBaseUrl = swapiBaseUrl;

        headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        entity = new HttpEntity<>(headers);
    }
    @Override
    public StarshipDTO getStarshipById(String id) {
        String cacheKey = "vehicle_" + id;
        StarshipDTO dataCached = cacheService.get(cacheKey, StarshipDTO.class);
        if (dataCached != null) {
            return dataCached;
        }

        String url = swapiBaseUrl + "starships/" + id;
        try {
            ResponseEntity<SwapiResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiResponse.class);
            SwapiResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                dataCached = mapToStarshipDTO(response.getResult());
                cacheService.put(cacheKey, dataCached);
                return dataCached;
            }
        } catch (Exception e) {
            log.error("Error al obtener nave por id: " + id, e);
        }
        return null;
    }

    @Override
    public List<StarshipDTO> getStarships(int page, int limit) {
        String url = swapiBaseUrl + "starships/?page=" + page + "&limit=" + limit;
        try {
            ResponseEntity<SwapiShortResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiShortResponse.class);
            SwapiShortResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return response.getResults().stream()
                        .map(result -> getStarshipById(result.getUid()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error al obtener lista de naves.", e);
        }
        return null;
    }

    @Override
    public List<StarshipDTO> getStarhipsByName(String name) {
        String url = swapiBaseUrl + "starships/?name=" + name;
        try {
            ResponseEntity<SwapiListResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiListResponse.class);
            SwapiListResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return response.getResult().stream()
                        .map(this::mapToStarshipDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error al obtener lista de naves.", e);
        }
        return null;
    }

    private StarshipDTO mapToStarshipDTO(SwapiResult result){
        SwapiStarshipProperties prop = objectMapper.convertValue(result.getProperties(), SwapiStarshipProperties.class);
        StarshipDTO dto = new StarshipDTO();
        dto.setUid(result.getUid());
        dto.setName(prop.getName());
        dto.setModel(prop.getModel());
        dto.setStarshipClass(prop.getStarship_class());
        dto.setManufacturer(prop.getManufacturer());
        dto.setCostInCredits(prop.getCost_in_credits());
        dto.setLength(prop.getLength());
        dto.setCrew(prop.getCrew());
        dto.setPassengers(prop.getPassengers());
        dto.setMaxAtmospheringSpeed(prop.getMax_atmosphering_speed());
        dto.setHyperdriveRating(prop.getHyperdrive_rating());
        dto.setMGLT(prop.getMGLT());
        dto.setCargoCapacity(prop.getCargo_capacity());
        dto.setConsumables(prop.getConsumables());
        dto.setFilms(utilsService.transformUrls(prop.getFilms()));
        return dto;
    }
}
