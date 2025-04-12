package com.marcos.starwarsapi.service;

import com.marcos.starwarsapi.dto.PersonDTO;

import java.util.List;

public interface PeopleService {
    PersonDTO getPersonById(String id);
    List<PersonDTO> getPeople(int page, int limit);
    List<PersonDTO> getPeopleByName(String name);
}