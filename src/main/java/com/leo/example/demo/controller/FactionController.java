package com.leo.example.demo.controller;

import com.leo.example.demo.service.FactionService;
import org.springframework.web.bind.annotation.*;
import com.leo.example.demo.entities.Faction;
import org.springframework.http.ResponseEntity;

import java.util.*;

@RestController
@RequestMapping("/factions")
public class FactionController {
    private final FactionService service;
    public FactionController(FactionService service) { this.service = service; }

    @GetMapping public List<Faction> list() { return service.list(); }
    @PostMapping public Faction create(@RequestBody Faction f) { return service.create(f); }
    @GetMapping("/{id}") public Faction get(@PathVariable UUID id) { return service.get(id); }
    @PutMapping("/{id}") public Faction update(@PathVariable UUID id, @RequestBody Faction in) { return service.update(id, in); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable UUID id) { service.delete(id); return ResponseEntity.noContent().build(); }
}