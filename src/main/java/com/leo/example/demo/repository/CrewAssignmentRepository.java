package com.leo.example.demo.repository;

import com.leo.example.demo.entities.CrewAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CrewAssignmentRepository extends JpaRepository<CrewAssignment, UUID> {
    List<CrewAssignment> findByShip_Id(UUID shipId);
    List<CrewAssignment> findByCharacter_Id(UUID characterId);
}
