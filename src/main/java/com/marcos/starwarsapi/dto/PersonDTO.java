package com.marcos.starwarsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private String uid;
    private String name;
    private String gender;
    private String skinColor;
    private String hairColor;
    private String height;
    private String eyeColor;
    private String mass;
    private String birthYear;
    private String homeworld;
    private String url;
}