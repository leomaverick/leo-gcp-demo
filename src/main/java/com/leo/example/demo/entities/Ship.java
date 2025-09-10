package com.leo.example.demo.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ship")
public class Ship {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faction_id")
    private Faction faction;

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Faction getFaction() { return faction; }
    public void setFaction(Faction faction) { this.faction = faction; }
}
