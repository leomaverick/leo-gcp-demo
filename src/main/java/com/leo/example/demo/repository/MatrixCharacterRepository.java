package com.leo.example.demo.repository;

import com.leo.example.demo.entities.MatrixCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface MatrixCharacterRepository extends JpaRepository<MatrixCharacter, UUID> {
    List<MatrixCharacter> findByFaction_Id(UUID factionId);
}




