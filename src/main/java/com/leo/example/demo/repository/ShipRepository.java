package com.leo.example.demo.repository;

import com.leo.example.demo.entities.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShipRepository extends JpaRepository<Ship, UUID> { }