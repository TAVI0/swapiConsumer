package com.marcos.starwarsapi.controller;

import com.marcos.starwarsapi.dto.PersonDTO;
import com.marcos.starwarsapi.service.PeopleService;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Tag(name = "People", description = "Operaciones relacionadas con las personas del universo Star Wars")
@RestController
@RequestMapping("/api/people")
public class PeopleController {

    @Autowired
    private PeopleService peopleService;

    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Operation(
        summary = "Obtener persona por ID",
        description = "Retorna una persona de Star Wars correspondiente al ID proporcionado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Persona encontrada",
            content = @Content(schema = @Schema(implementation = PersonDTO.class))),
        @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPersonById(
            @Parameter(description = "ID de la persona", example = "2")
            @PathVariable String id) {
        PersonDTO person = peopleService.getPersonById(id);
        if (person != null) {
            return ResponseEntity.ok(person);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Buscar personas por nombre",
        description = "Retorna una lista de personas cuyo nombre coincida (parcial o totalmente) con el valor proporcionado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de personas encontrada",
            content = @Content(schema = @Schema(implementation = PersonDTO.class))),
        @ApiResponse(responseCode = "204", description = "No se encontraron personas")
    })
    @GetMapping("/name/")
    public ResponseEntity<List<PersonDTO>> getPeopleByName(
            @Parameter(description = "Nombre del personaje a buscar", example = "Luke Skywalker")
            @RequestParam(required = false) String name) {

        List<PersonDTO> people = peopleService.getPeopleByName(name);
        if (people != null && !people.isEmpty()) {
            return ResponseEntity.ok(people);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Listar personas con paginacion",
        description = "Lista personas con soporte para paginación."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de personas encontrada",
            content = @Content(schema = @Schema(implementation = PersonDTO.class))),
        @ApiResponse(responseCode = "204", description = "No se encontraron personas")
    })
    @GetMapping()
    public ResponseEntity<List<PersonDTO>> getPeople(
            @Parameter(description = "Número de página (empezando en 1)", example = "1")
            @RequestParam(required = false, defaultValue = "1") int page,
            @Parameter(description = "Cantidad de elementos por página", example = "10")
            @RequestParam(required = false, defaultValue = "10") int limit
            ) {

        List<PersonDTO> people = peopleService.getPeople(page, limit);
        if (people != null && !people.isEmpty()) {
            return ResponseEntity.ok(people);
        }
        return ResponseEntity.noContent().build();
    }
}