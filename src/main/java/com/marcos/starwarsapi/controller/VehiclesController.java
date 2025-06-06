package com.marcos.starwarsapi.controller;

import com.marcos.starwarsapi.dto.PaginatedEntity;
import com.marcos.starwarsapi.dto.VehicleDTO;
import com.marcos.starwarsapi.service.VehiclesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Tag(name = "Vehicle", description = "Operaciones relacionadas con las vehiculos del universo Star Wars")
@RestController
@RequestMapping("/api/vehicles")
public class VehiclesController {

    @Autowired
    private VehiclesService vehiclesServices;
    @Value("${base-url}") String baseUrl;

    @Operation(summary = "Obtener una vahiculo espacial por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vehiculo encontrada",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = VehicleDTO.class))),
        @ApiResponse(responseCode = "404", description = "Vehiculo no encontrada",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(
            @Parameter(description = "ID de la vehiculo a buscar", example = "4")
            @PathVariable String id) {
        VehicleDTO vehicle = vehiclesServices.getVehicleById(id);
        if(vehicle!=null){
            return ResponseEntity.ok(vehicle);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar vahiculo por nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehiculos encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VehicleDTO.class))),
            @ApiResponse(responseCode = "204", description = "Sin resultados",
                    content = @Content)
    })
    @GetMapping("/name/")
    public ResponseEntity<List<VehicleDTO>> getPeopleByName(
            @Parameter(description = "Nombre de la nave a buscar", example = "Sand Crawler")
            @RequestParam(required = false) String name) {

        List<VehicleDTO> vehicles = vehiclesServices.getVehiclesByName(name);
        if (vehicles != null && !vehicles.isEmpty()) {
            return ResponseEntity.ok(vehicles);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todas las naves con paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de naves",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = VehicleDTO.class))),
        @ApiResponse(responseCode = "204", description = "Sin resultados",
            content = @Content)
    })
    @GetMapping()
    public ResponseEntity<PaginatedEntity> getVehicle(
        @Parameter(description = "Número de página (empezando en 1)", example = "1")
        @RequestParam(required = false, defaultValue = "1") int page,
        @Parameter(description = "Cantidad de elementos por página", example = "10")
        @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        List<VehicleDTO> vehicles = vehiclesServices.getVehicles(page, limit);
        if(vehicles!=null && !vehicles.isEmpty()){
            PaginatedEntity pe = new PaginatedEntity();
            if(page>=2) {
                int newpage = page - 1;
                pe.setPreviousPage(baseUrl + "vehicles?page=" + newpage + "&limit="+limit);
            }else{
                pe.setPreviousPage(null);
            }
            int nexpage = page+1;
            pe.setNextPage(baseUrl + "vehicles?page=" + nexpage + "&limit="+limit);
            pe.setEntities(vehicles);
            return ResponseEntity.ok(pe);
        }
        return ResponseEntity.noContent().build();
    }
}
