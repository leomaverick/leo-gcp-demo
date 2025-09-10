package com.leo.example.demo.service;

import com.leo.example.demo.entities.Faction;
import com.leo.example.demo.entities.Ship;
import com.leo.example.demo.repository.FactionRepository;
import com.leo.example.demo.repository.ShipRepository;
import com.leo.example.demo.util.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional
public class ShipService {
    private final ShipRepository shipRepo;
    private final FactionRepository factionRepo;

    public ShipService(ShipRepository shipRepo, FactionRepository factionRepo) {
        this.shipRepo = shipRepo; this.factionRepo = factionRepo;
    }

    @Transactional(readOnly = true)
    public List<Ship> list() { return shipRepo.findAll(); }

    public Ship create(Ship in) {
        if (in.getFaction() != null && in.getFaction().getId() != null) {
            Faction f = factionRepo.findById(in.getFaction().getId())
                    .orElseThrow(() -> new NotFoundException("Faction %s not found".formatted(in.getFaction().getId())));
            in.setFaction(f);
        }
        return shipRepo.save(in);
    }

    @Transactional(readOnly = true)
    public Ship get(UUID id) {
        return shipRepo.findById(id).orElseThrow(() -> new NotFoundException("Ship %s not found".formatted(id)));
    }

    public Ship update(UUID id, Ship in) {
        Ship s = get(id);
        s.setName(in.getName());
        if (in.getFaction() != null && in.getFaction().getId() != null) {
            Faction f = factionRepo.findById(in.getFaction().getId())
                    .orElseThrow(() -> new NotFoundException("Faction %s not found".formatted(in.getFaction().getId())));
            s.setFaction(f);
        }
        return shipRepo.save(s);
    }

    public void delete(UUID id) {
        if (!shipRepo.existsById(id)) throw new NotFoundException("Ship %s not found".formatted(id));
        shipRepo.deleteById(id);
    }
}

