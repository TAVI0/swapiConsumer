package com.marcos.starwarsapi.service;

import com.marcos.starwarsapi.dto.StarshipDTO;
import com.marcos.starwarsapi.dto.external.person.SwapiPeopleResponse;
import com.marcos.starwarsapi.dto.external.person.shortResponse.SwapiPeopleShortResponse;
import com.marcos.starwarsapi.dto.external.starship.SwapiStarshipProperties;
import com.marcos.starwarsapi.dto.external.starship.SwapiStarshipResponse;
import com.marcos.starwarsapi.dto.external.starship.SwapiStarshipResult;
import com.marcos.starwarsapi.dto.external.starship.SwapiStarshipsResponse;
import com.marcos.starwarsapi.dto.external.starship.shortResponse.SwapiStarshipsShortResponse;
import lombok.extern.slf4j.Slf4j;
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

    private final RestTemplate restTemplate;
    private final String swapiBaseUrl;
    HttpHeaders headers;
    HttpEntity<String> entity;

    public StarshipsServiceImp(RestTemplate restTemplate, @Value("${swapi.base-url}") String swapiBaseUrl) {
        this.restTemplate = restTemplate;
        this.swapiBaseUrl = swapiBaseUrl;

        headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        entity = new HttpEntity<>(headers);
    }
    @Override
    public StarshipDTO getStarshipById(String id) {
        String url = swapiBaseUrl + "starships/" + id;
        try {
            ResponseEntity<SwapiStarshipResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiStarshipResponse.class);
            SwapiStarshipResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return mapToStarshipDTO(response.getResult());
            }
        } catch (Exception e) {
            log.error("Error al obtener starship por id: " + id, e);
        }
        return null;
    }

    @Override
    public List<StarshipDTO> getStarships(int page, int limit) {
        String url = swapiBaseUrl + "starships/?page=" + page + "&limit=" + limit;
        try {
            ResponseEntity<SwapiStarshipsShortResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiStarshipsShortResponse.class);
            SwapiStarshipsShortResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return response.getResults().stream()
                        .map(result -> getStarshipById(result.getUid()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error al obtener lista de starships.", e);
        }
        return null;
    }

    @Override
    public List<StarshipDTO> getStarhipsByName(String name) {
        String url = swapiBaseUrl + "starships/?name=" + name;
        try {
            ResponseEntity<SwapiStarshipsResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiStarshipsResponse.class);
            SwapiStarshipsResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return response.getResult().stream()
                        .map(this::mapToStarshipDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error al obtener lista de starships.", e);
        }
        return null;
    }

    private StarshipDTO mapToStarshipDTO(SwapiStarshipResult result){
        SwapiStarshipProperties prop = result.getProperties();
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
        dto.setFilms(prop.getFilms());
        return dto;
    }
}
