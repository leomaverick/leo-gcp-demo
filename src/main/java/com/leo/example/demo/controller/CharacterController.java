package com.leo.example.demo.controller;

import com.leo.example.demo.entities.MatrixCharacter;
import com.leo.example.demo.service.MatrixCharacterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/characters")
public class CharacterController {
    private final MatrixCharacterService service;
    public CharacterController(MatrixCharacterService service) { this.service = service; }

    @GetMapping public List<MatrixCharacter> list(@RequestParam(required=false) UUID factionId) { return service.list(factionId); }
    @PostMapping public MatrixCharacter create(@RequestBody MatrixCharacter in) { return service.create(in); }
    @GetMapping("/{id}") public MatrixCharacter get(@PathVariable UUID id) { return service.get(id); }
    @PutMapping("/{id}") public MatrixCharacter update(@PathVariable UUID id, @RequestBody MatrixCharacter in) { return service.update(id, in); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable UUID id) { service.delete(id); return ResponseEntity.noContent().build(); }
}