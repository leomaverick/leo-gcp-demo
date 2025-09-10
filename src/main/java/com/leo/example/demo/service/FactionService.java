package com.leo.example.demo.service;

import com.leo.example.demo.entities.Faction;
import com.leo.example.demo.repository.FactionRepository;
import com.leo.example.demo.util.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional
public class FactionService {
    private final FactionRepository repo;
    public FactionService(FactionRepository repo) { this.repo = repo; }

    @Transactional(readOnly = true)
    public List<Faction> list() { return repo.findAll(); }

    public Faction create(Faction in) { return repo.save(in); }

    @Transactional(readOnly = true)
    public Faction get(UUID id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Faction %s not found".formatted(id)));
    }

    public Faction update(UUID id, Faction in) {
        Faction f = get(id);
        f.setName(in.getName());
        f.setDescription(in.getDescription());
        return repo.save(f);
    }

    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new NotFoundException("Faction %s not found".formatted(id));
        repo.deleteById(id);
    }
}