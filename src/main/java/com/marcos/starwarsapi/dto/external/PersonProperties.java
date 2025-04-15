package com.marcos.starwarsapi.dto.external;

import lombok.Data;

@Data
public class PersonProperties {
    private String name;
    private String gender;
    private String skin_color;
    private String hair_color;
    private String height;
    private String eye_color;
    private String mass;
    private String birth_year;
    private String homeworld;
    private String url;
}
