package com.marcos.starwarsapi.service;

import com.marcos.starwarsapi.dto.FilmDTO;
import com.marcos.starwarsapi.dto.external.film.SwapiFilmProperties;
import com.marcos.starwarsapi.dto.external.film.SwapiFilmResponse;
import com.marcos.starwarsapi.dto.external.film.SwapiFilmResult;
import com.marcos.starwarsapi.dto.external.film.SwapiFilmsResponse;
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
    private UtilsService utilsService;

    private final RestTemplate restTemplate;
    private final String swapiBaseUrl;
    HttpHeaders headers;
    HttpEntity<String> entity;

    public FilmsServiceImp(RestTemplate restTemplate, @Value("${swapi-url}") String swapiBaseUrl) {
        this.restTemplate = restTemplate;
        this.swapiBaseUrl = swapiBaseUrl;

        headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        entity = new HttpEntity<>(headers);
    }
    @Override
    public FilmDTO getFilmById(String id) {
        String url = swapiBaseUrl + "films/" + id;
        try {
            ResponseEntity<SwapiFilmResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiFilmResponse.class);
            SwapiFilmResponse response = responseEntity.getBody();
            if (response != null && "ok".equalsIgnoreCase(response.getMessage())) {
                return mapToFilmDTO(response.getResult());
            }
        } catch (Exception e) {
            log.error("Error al obtener pelicula por id: " + id, e);
        }
        return null;
    }

    @Override
    public List<FilmDTO> getFilms(int page, int limit) {
        String url = swapiBaseUrl + "films/";

        try {
            ResponseEntity<SwapiFilmsResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiFilmsResponse.class);
            SwapiFilmsResponse response = responseEntity.getBody();
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
        String url = swapiBaseUrl + "films/?title=" + title;
        try {
            ResponseEntity<SwapiFilmsResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, SwapiFilmsResponse.class);
            SwapiFilmsResponse response = responseEntity.getBody();
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

    private FilmDTO mapToFilmDTO(SwapiFilmResult result){
        SwapiFilmProperties prop = result.getProperties();
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
