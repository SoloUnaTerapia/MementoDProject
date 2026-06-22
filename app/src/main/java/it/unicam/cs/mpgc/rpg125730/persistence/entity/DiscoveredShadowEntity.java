package it.unicam.cs.mpgc.rpg125730.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entità JPA/Hibernate che rappresenta un record nel Compendio delle Ombre
 * Viene salvata nel database globale per tenere traccia delle ombre scoperte tra le varie partite
 */
@Entity
@Table(name = "COMPENDIO_OMBRE")
public class DiscoveredShadowEntity {

    @Id
    private String name;

    private String weakness;
    private String resistance;
    private int encounterCount;


    protected DiscoveredShadowEntity() {}

    public DiscoveredShadowEntity(String name, String weakness, String resistance) {
        this.name = name;
        this.weakness = weakness != null ? weakness : "Nessuna";
        this.resistance = resistance != null ? resistance : "Nessuna";
        this.encounterCount = 1;
    }

    //ogni volta che incontro l'ombra aumento le volte che l'ho incontrata
    public void incrementEncounters() {
        this.encounterCount++;
    }


    public String getName() { return name; }
    public String getWeakness() { return weakness; }
    public String getResistance() { return resistance; }
    public int getEncounterCount() { return encounterCount; }
}