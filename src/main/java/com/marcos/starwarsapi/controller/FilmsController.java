package com.marcos.starwarsapi.controller;

import com.marcos.starwarsapi.dto.FilmDTO;
import com.marcos.starwarsapi.service.FilmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Film", description = "Operaciones relacionadas con las peliculas del universo Star Wars")
@RestController
@RequestMapping("/api/films")
public class FilmsController {

    @Autowired
    private FilmsService filmsService;

    @Operation(summary = "Obtener una pelicula espacial por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pelicula encontrada",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = FilmDTO.class))),
        @ApiResponse(responseCode = "404", description = "Pelicula no encontrada",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<FilmDTO> getFilmById(
            @Parameter(description = "ID de la vehiculo a buscar", example = "1")
            @PathVariable String id) {
        FilmDTO film = filmsService.getFilmById(id);
        if(film!=null){
            return ResponseEntity.ok(film);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar peliculas por nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "peliculas encontrados",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = FilmDTO.class))),
        @ApiResponse(responseCode = "204", description = "Sin resultados",
            content = @Content)
    })
    @GetMapping("/name/")
    public ResponseEntity<List<FilmDTO>> getFilmsByTitle(
        @Parameter(description = "Nombre de la pelicula a buscar", example = "A New Hope")
        @RequestParam(required = false) String title) {

        List<FilmDTO> films = filmsService.getFilmsByTitle(title);
        if (films != null && !films.isEmpty()) {
            return ResponseEntity.ok(films);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todas las peliculas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de peliculas",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = FilmDTO.class))),
        @ApiResponse(responseCode = "204", description = "Sin resultados",
            content = @Content)
    })
    @GetMapping()
    public ResponseEntity<List<FilmDTO>> getFilms() {
        List<FilmDTO> films = filmsService.getFilms(0, 0);
        if(films!=null && !films.isEmpty()){
            return ResponseEntity.ok(films);
        }
        return ResponseEntity.noContent().build();
    }
}
