package com.leo.example.demo.service;

import com.leo.example.demo.entities.CrewAssignment;
import com.leo.example.demo.entities.MatrixCharacter;
import com.leo.example.demo.entities.Ship;
import com.leo.example.demo.repository.CrewAssignmentRepository;
import com.leo.example.demo.repository.MatrixCharacterRepository;
import com.leo.example.demo.repository.ShipRepository;
import com.leo.example.demo.util.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional
public class CrewAssignmentService {
    private final CrewAssignmentRepository crewRepo;
    private final ShipRepository shipRepo;
    private final MatrixCharacterRepository charRepo;

    public CrewAssignmentService(CrewAssignmentRepository crewRepo,
                                 ShipRepository shipRepo,
                                 MatrixCharacterRepository charRepo) {
        this.crewRepo = crewRepo; this.shipRepo = shipRepo; this.charRepo = charRepo;
    }

    public CrewAssignment addCrew(UUID shipId, UUID characterId, String role) {
        Ship ship = shipRepo.findById(shipId)
                .orElseThrow(() -> new NotFoundException("Ship %s not found".formatted(shipId)));
        MatrixCharacter mc = charRepo.findById(characterId)
                .orElseThrow(() -> new NotFoundException("Character %s not found".formatted(characterId)));
        CrewAssignment ca = new CrewAssignment();
        ca.setShip(ship); ca.setCharacter(mc); ca.setRole(role);
        return crewRepo.save(ca);
    }

    @Transactional(readOnly = true)
    public List<CrewAssignment> listByShip(UUID shipId) {
        return crewRepo.findByShip_Id(shipId);
    }

    @Transactional(readOnly = true)
    public List<CrewAssignment> listByCharacter(UUID characterId) {
        return crewRepo.findByCharacter_Id(characterId);
    }

    public void remove(UUID assignmentId) {
        if (!crewRepo.existsById(assignmentId)) throw new NotFoundException("Assignment %s not found".formatted(assignmentId));
        crewRepo.deleteById(assignmentId);
    }
}

