package com.marcos.starwarsapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcos.starwarsapi.dto.PersonDTO;
import com.marcos.starwarsapi.dto.external.PersonProperties;
import com.marcos.starwarsapi.dto.external.swapi.SwapiListResponse;
import com.marcos.starwarsapi.dto.external.swapi.SwapiResponse;
import com.marcos.starwarsapi.dto.external.swapi.SwapiResult;
import com.marcos.starwarsapi.dto.external.swapi.SwapiShortResponse;
import com.marcos.starwarsapi.service.utiles.CacheService;
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
public class PeopleServiceImp implements PeopleService {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CacheService cacheService;

    private final RestTemplate restTemplate;
    private final String swapiBaseUrl;
    HttpHeaders headers;
    HttpEntity<String> entity;
    public PeopleServiceImp(RestTemplate restTemplate, @Value("${swapi-url}") String swapiBaseUrl) {
        this.restTemplate = restTemplate;
        this.swapiBaseUrl = swapiBaseUrl;

        headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        entity = new HttpEntity<>(headers);
    }

    @Override
    public PersonDTO getPersonById(String id) {
        String cacheKey = "person_" + id;
        PersonDTO dataCached = cacheService.get(cacheKey, PersonDTO.class);
        if (dataCached != null) {
            return dataCached;
        }

        String url = swapiBaseUrl + "people/" + id;
        try {
            ResponseEntity<SwapiResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiResponse.class);
            SwapiResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                dataCached =  mapToPersonDTO(response.getResult());
                cacheService.put(cacheKey, dataCached);
                return dataCached;
            }
        } catch (Exception e) {
            log.error("Error al obtener persona por id: " + id, e);
        }
        return null;
    }


    @Override
    public List<PersonDTO> getPeople(int page, int limit) {
        String url = swapiBaseUrl + "people/?page=" + page + "&limit=" + limit;
        try {
            ResponseEntity<SwapiShortResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiShortResponse.class);
            SwapiShortResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return response.getResults().stream()
                        .map(result -> getPersonById(result.getUid()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error al obtener lista de personas.", e);
        }
        return null;
    }

    @Override
    public List<PersonDTO> getPeopleByName(String name) {
        String url = swapiBaseUrl + "people/?name=" + name;
        try {
            ResponseEntity<SwapiListResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiListResponse.class);
            SwapiListResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return response.getResult().stream()
                        .map(this::mapToPersonDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error al obtener lista de personas.", e);
        }
        return null;
    }

    private PersonDTO mapToPersonDTO(SwapiResult result) {
        PersonProperties prop = objectMapper.convertValue(result.getProperties(), PersonProperties.class);
        PersonDTO dto = new PersonDTO();
        dto.setUid(result.getUid());
        dto.setName(prop.getName());
        dto.setGender(prop.getGender());
        dto.setSkinColor(prop.getSkin_color());
        dto.setHairColor(prop.getHair_color());
        dto.setHeight(prop.getHeight());
        dto.setEyeColor(prop.getEye_color());
        dto.setMass(prop.getMass());
        dto.setBirthYear(prop.getBirth_year());
        dto.setHomeworld(prop.getHomeworld());
        dto.setUrl(prop.getUrl());
        return dto;
    }

}