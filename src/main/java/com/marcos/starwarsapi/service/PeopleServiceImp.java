package com.marcos.starwarsapi.service;

import com.marcos.starwarsapi.dto.PersonDTO;
import com.marcos.starwarsapi.dto.external.person.SwapiPeopleResponse;
import com.marcos.starwarsapi.dto.external.person.SwapiPersonProperties;
import com.marcos.starwarsapi.dto.external.person.SwapiPersonResponse;
import com.marcos.starwarsapi.dto.external.person.SwapiPersonResult;
import com.marcos.starwarsapi.dto.external.person.shortResponse.SwapiPeopleShortResponse;
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
public class PeopleServiceImp implements PeopleService {

    private final RestTemplate restTemplate;
    private final String swapiBaseUrl;
    HttpHeaders headers;
    HttpEntity<String> entity;
    public PeopleServiceImp(RestTemplate restTemplate, @Value("${swapi.base-url}") String swapiBaseUrl) {
        this.restTemplate = restTemplate;
        this.swapiBaseUrl = swapiBaseUrl;

        headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        entity = new HttpEntity<>(headers);
    }

    @Override
    public PersonDTO getPersonById(String id) {
        String url = swapiBaseUrl + "people/" + id;
        try {
            ResponseEntity<SwapiPersonResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiPersonResponse.class);
            SwapiPersonResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return mapToPersonDTO(response.getResult());
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
            ResponseEntity<SwapiPeopleShortResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiPeopleShortResponse.class);
            SwapiPeopleShortResponse response = responseEntity.getBody();
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
            ResponseEntity<SwapiPeopleResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiPeopleResponse.class);
            SwapiPeopleResponse response = responseEntity.getBody();
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

    private PersonDTO mapToPersonDTO(SwapiPersonResult result) {
        SwapiPersonProperties prop = result.getProperties();
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