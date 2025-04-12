package com.marcos.starwarsapi.service;

import com.marcos.starwarsapi.dto.StarshipDTO;

import java.util.List;

public interface StarshipsService {
    StarshipDTO getStarshipById(String id);
    List<StarshipDTO> getStarships(int page, int limit);
    List<StarshipDTO> getStarhipsByName(String name);
}
