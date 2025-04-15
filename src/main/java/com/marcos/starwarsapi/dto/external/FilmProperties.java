package com.marcos.starwarsapi.dto.external;

import lombok.Data;

import java.util.List;

@Data
public class FilmProperties {
    private List<String> starships;
    private List<String> vehicles;
    private List<String> planets;
    private String producer;
    private String title;
    private int episode_id;
    private String director;
    private String release_date;
    private String opening_crawl;
    private List<String> characters;
    private List<String> species;
    private String created;
    private String edited;
    private String url;
}
