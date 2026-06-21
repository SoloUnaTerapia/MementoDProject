package it.unicam.cs.mpgc.rpg125730.dto;

import it.unicam.cs.mpgc.rpg125730.model.Element;

/**
 * Record utilizzato per mappare i dati dei mostri letti dal file JSON.
 */
public record ShadowDataDTO(
        String name,
        int baseHp,
        int baseDamage,
        Element weakness,
        Element resistance
) {}