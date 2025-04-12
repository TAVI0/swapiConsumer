package com.marcos.starwarsapi.controller;

import com.marcos.starwarsapi.dto.StarshipDTO;
import com.marcos.starwarsapi.service.StarshipsService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/starships")
public class StarshipsController {

    private final StarshipsService starshipsService;

    public StarshipsController(StarshipsService starshipsService) {
        this.starshipsService = starshipsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<StarshipDTO> getStarshipById(@PathVariable String id){
        StarshipDTO starship = starshipsService.getStarshipById(id);
        if(starship!=null){
            return ResponseEntity.ok(starship);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/name/")
    public ResponseEntity<List<StarshipDTO>> getPeopleByName(
            @RequestParam(required = false) String name) {

        List<StarshipDTO> starhips = starshipsService.getStarhipsByName(name);
        if (starhips != null && !starhips.isEmpty()) {
            return ResponseEntity.ok(starhips);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<StarshipDTO>> getStarship(
        @Parameter(description = "Número de página (empezando en 1)")
        @RequestParam(required = false, defaultValue = "1") int page,
        @Parameter(description = "Cantidad de elementos por página")
        @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        List<StarshipDTO> starships = starshipsService.getStarships(page, limit);
        if(starships!=null && !starships.isEmpty()){
            return ResponseEntity.ok(starships);
        }
        return ResponseEntity.noContent().build();
    }
}
