package com.marcos.starwarsapi.FilmsTest;

import com.marcos.starwarsapi.dto.FilmDTO;
import com.marcos.starwarsapi.dto.external.film.SwapiFilmProperties;
import com.marcos.starwarsapi.dto.external.film.SwapiFilmResponse;
import com.marcos.starwarsapi.dto.external.film.SwapiFilmResult;
import com.marcos.starwarsapi.dto.external.film.SwapiFilmsResponse;
import com.marcos.starwarsapi.service.FilmsServiceImp;
import com.marcos.starwarsapi.service.utiles.CacheService;
import com.marcos.starwarsapi.service.utiles.UtilsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilmsServiceImpTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UtilsService utilsService;

    @Mock
    private CacheService cacheService;
    @InjectMocks
    private FilmsServiceImp filmsService;

    private final String baseUrl = "https://swapi.tech/api/";

    @BeforeEach
    void setUp() {
        filmsService = new FilmsServiceImp(restTemplate, baseUrl);
        ReflectionTestUtils.setField(filmsService, "utilsService", utilsService);
        ReflectionTestUtils.setField(filmsService, "cacheService", cacheService);

    }

    @Test
    void testGetFilmById_ReturnsFilmDTO_WhenFilmExists() {
        String filmId = "1";
        when(cacheService.get("film_1", FilmDTO.class)).thenReturn(null);

        SwapiFilmProperties properties = new SwapiFilmProperties();
        properties.setTitle("A New Hope");
        properties.setEpisode_id(4);
        SwapiFilmResult result = new SwapiFilmResult();
        result.setUid(filmId);
        result.setProperties(properties);
        SwapiFilmResponse response = new SwapiFilmResponse();
        response.setMessage("ok");
        response.setResult(result);

        ResponseEntity<SwapiFilmResponse> entity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(SwapiFilmResponse.class)))
                .thenReturn(entity);

        FilmDTO filmDTO = filmsService.getFilmById(filmId);

        assertNotNull(filmDTO);
        assertEquals("A New Hope", filmDTO.getTitle());
        assertEquals(4, filmDTO.getEpisodeId());
    }

    @Test
    void testGetFilmsByTitle_ReturnsEmptyList_WhenNoFilm() {
        String title = "FakeTitle";
        SwapiFilmsResponse response = new SwapiFilmsResponse();
        response.setMessage("ok");
        response.setResult(Collections.emptyList());

        ResponseEntity<SwapiFilmsResponse> entity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(SwapiFilmsResponse.class)))
                .thenReturn(entity);

        List<FilmDTO> result = filmsService.getFilmsByTitle(title);
        assertTrue(result.isEmpty());
    }
}
