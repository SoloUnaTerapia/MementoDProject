package it.unicam.cs.mpgc.rpg125730.persistence;

import it.unicam.cs.mpgc.rpg125730.dto.GameStateDTO;

/**
 * Definisce i contratti per il salvataggio e caricamento della partita.
 * (Rispetto della Dependency Inversion: il Controller parlerà con questa interfaccia, non con la classe JSON).
 */
public interface PersistenceService {
    void saveGame(GameStateDTO state) throws Exception;
    GameStateDTO loadGame() throws Exception;
}