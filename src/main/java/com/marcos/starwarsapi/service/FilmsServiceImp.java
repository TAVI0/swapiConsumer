package com.marcos.starwarsapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcos.starwarsapi.dto.FilmDTO;
import com.marcos.starwarsapi.dto.external.SwapiFilmProperties;
import com.marcos.starwarsapi.dto.external.swapi.SwapiListResponse;
import com.marcos.starwarsapi.dto.external.swapi.SwapiResponse;
import com.marcos.starwarsapi.dto.external.swapi.SwapiResult;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmsServiceImp implements FilmsService{

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

    public FilmsServiceImp(RestTemplate restTemplate, @Value("${swapi-url}") String swapiBaseUrl) {
        this.restTemplate = restTemplate;
        this.swapiBaseUrl = swapiBaseUrl + "films/";

        headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        entity = new HttpEntity<>(headers);
    }
    @Override
    public FilmDTO getFilmById(String id) {
        String cacheKey = "film_" + id;
        FilmDTO dataCached = cacheService.get(cacheKey, FilmDTO.class);
        if (dataCached != null) {
            return dataCached;
        }

        String url = swapiBaseUrl + id;
        try {
            ResponseEntity<SwapiResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiResponse.class);
            SwapiResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                dataCached = mapToFilmDTO(response.getResult());
                cacheService.put(cacheKey, dataCached);
                return dataCached;
            }
        } catch (Exception e) {
            log.error("Error al obtener pelicula por id: " + id, e);
        }
        return null;
    }

    @Override
    public List<FilmDTO> getFilms(int page, int limit) {
        try {
            ResponseEntity<SwapiListResponse> responseEntity = restTemplate.exchange(swapiBaseUrl, HttpMethod.GET, entity, SwapiListResponse.class);
            SwapiListResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return response.getResult().stream()
                        .map(this::mapToFilmDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error al obtener lista de peliculas.", e);
        }
        return null;
    }

    @Override
    public List<FilmDTO> getFilmsByTitle(String title) {
        String url = swapiBaseUrl + "?title=" + title;
        try {
            ResponseEntity<SwapiListResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiListResponse.class);
            SwapiListResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return response.getResult().stream()
                        .map(this::mapToFilmDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("Error al obtener lista de peliculas.", e);
        }
        return null;
    }

    private FilmDTO mapToFilmDTO(SwapiResult result){
        SwapiFilmProperties prop = objectMapper.convertValue(result.getProperties(), SwapiFilmProperties.class);
        FilmDTO dto = new FilmDTO();
        dto.setUid(result.getUid());
        dto.setStarships(utilsService.transformUrls(prop.getStarships()));
        dto.setVehicles(utilsService.transformUrls(prop.getVehicles()));
        dto.setPlanets(prop.getPlanets());
        dto.setProducer(prop.getProducer());
        dto.setTitle(prop.getTitle());
        dto.setEpisodeId(prop.getEpisode_id());
        dto.setDirector(prop.getDirector());
        dto.setReleaseDate(prop.getRelease_date());
        dto.setOpeningCrawl(prop.getOpening_crawl());
        dto.setCharacters(utilsService.transformUrls(prop.getCharacters()));
        dto.setSpecies(prop.getSpecies());
        dto.setCreated(prop.getCreated());
        dto.setEdited(prop.getEdited());
        dto.setUrl(prop.getUrl());
        return dto;
    }
}
