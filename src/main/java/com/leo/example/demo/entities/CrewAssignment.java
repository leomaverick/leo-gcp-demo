package com.leo.example.demo.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(
        name = "crew_assignment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"character_id","ship_id"})
)
public class CrewAssignment {
    @Id @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "character_id")
    private MatrixCharacter character;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ship_id")
    private Ship ship;

    @Column(nullable = false)
    private String role; // e.g., CAPTAIN, OPERATOR, CREW

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public MatrixCharacter getCharacter() { return character; }
    public void setCharacter(MatrixCharacter character) { this.character = character; }
    public Ship getShip() { return ship; }
    public void setShip(Ship ship) { this.ship = ship; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
