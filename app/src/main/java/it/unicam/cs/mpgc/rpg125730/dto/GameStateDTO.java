package it.unicam.cs.mpgc.rpg125730.dto;

/**
 * Record (DTO) che rappresenta "lo screenshot" della partita corrente
 * Utilizzato esclusivamente per trasportare i dati dal Controller al file di salvataggio.
 */
public record GameStateDTO(
        int roomLevel,
        int playerSanity,
        int playerFocus,
        int enemySanity,
        String enemyName
) {}