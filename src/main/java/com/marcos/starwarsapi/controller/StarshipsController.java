package com.marcos.starwarsapi.controller;

import com.marcos.starwarsapi.dto.StarshipDTO;
import com.marcos.starwarsapi.service.StarshipsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Starship", description = "Operaciones relacionadas con las naves del universo Star Wars")
@RestController
@RequestMapping("/api/starships")
public class StarshipsController {

    private final StarshipsService starshipsService;

    public StarshipsController(StarshipsService starshipsService) {
        this.starshipsService = starshipsService;
    }

    @Operation(summary = "Obtener una nave espacial por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nave encontrada",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = StarshipDTO.class))),
        @ApiResponse(responseCode = "404", description = "Nave no encontrada",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<StarshipDTO> getStarshipById(
            @Parameter(description = "ID de la nave a buscar", example = "2")
            @PathVariable String id) {
        StarshipDTO starship = starshipsService.getStarshipById(id);
        if(starship!=null){
            return ResponseEntity.ok(starship);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar naves por nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Naves encontradas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StarshipDTO.class))),
            @ApiResponse(responseCode = "204", description = "Sin resultados",
                    content = @Content)
    })
    @GetMapping("/name/")
    public ResponseEntity<List<StarshipDTO>> getPeopleByName(
            @Parameter(description = "Nombre de la nave a buscar", example = "CR90 corvette")
            @RequestParam(required = false) String name) {

        List<StarshipDTO> starhips = starshipsService.getStarhipsByName(name);
        if (starhips != null && !starhips.isEmpty()) {
            return ResponseEntity.ok(starhips);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todas las naves con paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de naves",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = StarshipDTO.class))),
        @ApiResponse(responseCode = "204", description = "Sin resultados",
            content = @Content)
    })
    @GetMapping()
    public ResponseEntity<List<StarshipDTO>> getStarship(
        @Parameter(description = "Número de página (empezando en 1)", example = "1")
        @RequestParam(required = false, defaultValue = "1") int page,
        @Parameter(description = "Cantidad de elementos por página", example = "10")
        @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        List<StarshipDTO> starships = starshipsService.getStarships(page, limit);
        if(starships!=null && !starships.isEmpty()){
            return ResponseEntity.ok(starships);
        }
        return ResponseEntity.noContent().build();
    }
}
