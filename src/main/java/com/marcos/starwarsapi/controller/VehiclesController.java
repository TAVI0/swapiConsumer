package com.marcos.starwarsapi.controller;

import com.marcos.starwarsapi.dto.VehicleDTO;
import com.marcos.starwarsapi.service.VehiclesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vehicle", description = "Operaciones relacionadas con las vehiculos del universo Star Wars")
@RestController
@RequestMapping("/api/vehicles")
public class VehiclesController {

    private final VehiclesService vehiclesServices;

    public VehiclesController(VehiclesService vehiclesServices) {
        this.vehiclesServices = vehiclesServices;
    }

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
            @Parameter(description = "ID de la vehiculo a buscar", example = "2")
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
            @Parameter(description = "Nombre de la nave a buscar", example = "CR90 corvette")
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
    public ResponseEntity<List<VehicleDTO>> getVehicle(
        @Parameter(description = "Número de página (empezando en 1)", example = "1")
        @RequestParam(required = false, defaultValue = "1") int page,
        @Parameter(description = "Cantidad de elementos por página", example = "10")
        @RequestParam(required = false, defaultValue = "10") int limit
    ) {
        List<VehicleDTO> vehicles = vehiclesServices.getVehicles(page, limit);
        if(vehicles!=null && !vehicles.isEmpty()){
            return ResponseEntity.ok(vehicles);
        }
        return ResponseEntity.noContent().build();
    }
}
