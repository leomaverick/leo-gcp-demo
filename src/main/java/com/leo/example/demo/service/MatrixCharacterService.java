package com.leo.example.demo.service;

import com.leo.example.demo.entities.Faction;
import com.leo.example.demo.entities.MatrixCharacter;
import com.leo.example.demo.repository.FactionRepository;
import com.leo.example.demo.repository.MatrixCharacterRepository;
import com.leo.example.demo.util.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional
public class MatrixCharacterService {
    private final MatrixCharacterRepository repo;
    private final FactionRepository factionRepo;

    public MatrixCharacterService(MatrixCharacterRepository repo, FactionRepository factionRepo) {
        this.repo = repo; this.factionRepo = factionRepo;
    }

    @Transactional(readOnly = true)
    public List<MatrixCharacter> list(UUID factionId) {
        return (factionId == null) ? repo.findAll() : repo.findByFaction_Id(factionId);
    }

    public MatrixCharacter create(MatrixCharacter in) {
        if (in.getFaction() != null && in.getFaction().getId() != null) {
            Faction f = factionRepo.findById(in.getFaction().getId())
                    .orElseThrow(() -> new NotFoundException("Faction %s not found".formatted(in.getFaction().getId())));
            in.setFaction(f);
        }
        return repo.save(in);
    }

    @Transactional(readOnly = true)
    public MatrixCharacter get(UUID id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Character %s not found".formatted(id)));
    }

    public MatrixCharacter update(UUID id, MatrixCharacter in) {
        MatrixCharacter c = get(id);
        c.setName(in.getName());
        c.setAlias(in.getAlias());
        c.setHuman(in.isHuman());
        if (in.getFaction() != null && in.getFaction().getId() != null) {
            Faction f = factionRepo.findById(in.getFaction().getId())
                    .orElseThrow(() -> new NotFoundException("Faction %s not found".formatted(in.getFaction().getId())));
            c.setFaction(f);
        }
        return repo.save(c);
    }

    public void delete(UUID id) {
        if (!repo.existsById(id)) throw new NotFoundException("Character %s not found".formatted(id));
        repo.deleteById(id);
    }
}

