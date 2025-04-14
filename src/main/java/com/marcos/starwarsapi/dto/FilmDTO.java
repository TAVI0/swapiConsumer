package com.marcos.starwarsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmDTO {
    private String uid;
    private List<String> starships;
    private List<String> vehicles;
    private List<String> planets;
    private String producer;
    private String title;
    private int episodeId;
    private String director;
    private String releaseDate;
    private String openingCrawl;
    private List<String> characters;
    private List<String> species;
    private String created;
    private String edited;
    private String url;
}