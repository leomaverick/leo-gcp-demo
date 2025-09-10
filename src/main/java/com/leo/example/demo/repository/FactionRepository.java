package com.leo.example.demo.repository;

import com.leo.example.demo.entities.Faction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FactionRepository extends JpaRepository<Faction, UUID> { }
