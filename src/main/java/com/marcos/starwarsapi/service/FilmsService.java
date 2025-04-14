package com.marcos.starwarsapi.service;

import com.marcos.starwarsapi.dto.FilmDTO;
import com.marcos.starwarsapi.dto.VehicleDTO;

import java.util.List;

public interface FilmsService {
    FilmDTO getFilmById(String id);
    List<FilmDTO> getFilms(int page, int limit);
    List<FilmDTO> getFilmsByTitle(String title);
}
