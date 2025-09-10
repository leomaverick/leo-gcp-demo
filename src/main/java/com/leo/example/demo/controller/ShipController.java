package com.leo.example.demo.controller;

import com.leo.example.demo.entities.CrewAssignment;
import com.leo.example.demo.entities.Ship;
import com.leo.example.demo.record.CrewPayload;
import com.leo.example.demo.service.CrewAssignmentService;
import com.leo.example.demo.service.ShipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ships")
public class ShipController {
    private final ShipService shipService;
    private final CrewAssignmentService crewService;

    public ShipController(ShipService shipService, CrewAssignmentService crewService) {
        this.shipService = shipService; this.crewService = crewService;
    }

    @GetMapping
    public List<Ship> list() { return shipService.list(); }
    @PostMapping
    public Ship create(@RequestBody Ship in) { return shipService.create(in); }
    @GetMapping("/{id}") public Ship get(@PathVariable UUID id) { return shipService.get(id); }
    @PutMapping("/{id}") public Ship update(@PathVariable UUID id, @RequestBody Ship in) { return shipService.update(id, in); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable UUID id) { shipService.delete(id); return ResponseEntity.noContent().build(); }


    @PostMapping("/{shipId}/crew")
    public CrewAssignment addCrew(@PathVariable UUID shipId, @RequestBody CrewPayload payload) {
        return crewService.addCrew(shipId, payload.characterId(), payload.role());
    }

    @GetMapping("/{shipId}/crew")
    public List<CrewAssignment> listCrew(@PathVariable UUID shipId) {
        return crewService.listByShip(shipId);
    }

    @DeleteMapping("/crew/{assignmentId}")
    public ResponseEntity<Void> removeCrew(@PathVariable UUID assignmentId) {
        crewService.remove(assignmentId); return ResponseEntity.noContent().build();
    }
}
