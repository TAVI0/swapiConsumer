package com.marcos.starwarsapi.controller;

import com.marcos.starwarsapi.dto.PaginatedEntity;
import com.marcos.starwarsapi.dto.StarshipDTO;
import com.marcos.starwarsapi.service.StarshipsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Starship", description = "Operaciones relacionadas con las naves del universo Star Wars")
@RestController
@RequestMapping("/api/starships")
public class StarshipsController {

    @Autowired
    private StarshipsService starshipsService;
    @Value("${base-url}") String baseUrl;

    @Operation(
        summary = "Obtener una nave espacial por su ID",
        description = "Retorna una nave espacial correspondiente al ID proporcionado.")
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

    @Operation(
        summary = "Buscar naves por nombre",
        description = "Retorna una lista de naves cuyo nombre coincida (parcial o totalmente) con el valor proporcionado.")
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
            @RequestParam(required = true) String name) {

        List<StarshipDTO> starhips = starshipsService.getStarhipsByName(name);
        if (starhips != null && !starhips.isEmpty()) {
            return ResponseEntity.ok(starhips);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Listar todas las naves con paginación",
        description = "Lista naves con soporte para paginación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de naves",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = StarshipDTO.class))),
        @ApiResponse(responseCode = "204", description = "Sin resultados",
            content = @Content)
    })
    @GetMapping()
    public ResponseEntity<PaginatedEntity> getStarship(
        @Parameter(description = "Número de página (empezando en 1)", example = "1")
        @RequestParam(required = false, defaultValue = "1") int page,
        @Parameter(description = "Cantidad de elementos por página", example = "10")
        @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        List<StarshipDTO> starships = starshipsService.getStarships(page, limit);
        if(starships!=null && !starships.isEmpty()){
            PaginatedEntity pe = new PaginatedEntity();
            if(page>=2) {
                int newpage = page - 1;
                pe.setPreviousPage(baseUrl + "starships?page=" + newpage + "&limit="+limit);
            }else{
                pe.setPreviousPage(null);
            }
            int nexpage = page+1;
            pe.setNextPage(baseUrl + "starships?page=" + nexpage + "&limit="+limit);
            pe.setEntities(starships);
            return ResponseEntity.ok(pe);
        }
        return ResponseEntity.noContent().build();
    }
}
