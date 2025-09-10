package com.leo.example.demo.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "characters")
public class MatrixCharacter {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String alias;

    @Column(name = "is_human", nullable = false)
    private boolean human = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faction_id")
    private Faction faction;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public boolean isHuman() { return human; }
    public void setHuman(boolean human) { this.human = human; }
    public Faction getFaction() { return faction; }
    public void setFaction(Faction faction) { this.faction = faction; }
}
