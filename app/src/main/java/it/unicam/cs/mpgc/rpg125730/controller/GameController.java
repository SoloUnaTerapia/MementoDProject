package it.unicam.cs.mpgc.rpg125730.controller;

import it.unicam.cs.mpgc.rpg125730.model.Element;
import it.unicam.cs.mpgc.rpg125730.model.Player;
import it.unicam.cs.mpgc.rpg125730.model.Shadow;
import it.unicam.cs.mpgc.rpg125730.service.CombatManager;

/**
 * Gestisce il flusso della partita mantenendo lo stato del gioco (giocatore, nemico attuale)
 * e facendo da intermediario tra la UI e la logica di business
 */
public class GameController {

    private final CombatManager combatManager;
    private Player player;
    private Shadow currentShadow;
    private int roomLevel;

    public GameController() {
        this.combatManager = new CombatManager();
        startNewGame();
    }

    /**
     * Inizializza una nuova partita
     */
    public void startNewGame() {
        this.roomLevel = 1;
        this.player = new Player("Diver", 100, 50);
        spawnEnemyForCurrentRoom();
    }

    /**
     * Genera un nuovo nemico in base al livello della stanza
     */
    private void spawnEnemyForCurrentRoom() {
        //al momento generato direttamente ricordare di cambiarlo
        this.currentShadow = new Shadow(
                "Pixie",
                40 + (roomLevel*5),  //la vita aumenta con i livelli
                20,
                8 + roomLevel,         //il danno aumenta
                Element.FIRE,          //debole al fuoco
                Element.ICE            //resiste al ghiaccio
        );
    }

    // --- METODI CHIAMATI DALLA VIEW ---

    public String handlePhysicalAttack() {
        String log = combatManager.executePhysicalAttack(player, currentShadow, 15);
        return processEnemyTurn(log);
    }

    public String handleCognitiveSkill(Element skillElement) {
        //costo fisso di 10 Focus e un danno di 20 per l'abilità
        String log = combatManager.executeElementalAttack(player, currentShadow, skillElement, 10, 20);
        return processEnemyTurn(log);
    }

    public String handleStabilize() {
        //costa 15 Focus, cura 30 Sanity
        String log = combatManager.executeStabilize(player, 15, 30);
        return processEnemyTurn(log);
    }

    /**
     * Gestisce la risposta del nemico e controlla se qualcuno è morto.
     */
    private String processEnemyTurn(String playerActionLog) {
        StringBuilder resultLog = new StringBuilder(playerActionLog).append("\n");

        if (!currentShadow.isConscious()) {
            resultLog.append("Hai sconfitto l'Ombra! Avanzi alla stanza successiva.\n");
            roomLevel++;
            spawnEnemyForCurrentRoom();
            return resultLog.toString();
        }

        //se l'ombra è viva, attacca
        String enemyLog = combatManager.executeEnemyTurn(currentShadow, player);
        resultLog.append(enemyLog).append("\n");

        if (!player.isConscious()) {
            resultLog.append("\n--- LA TUA MENTE È COLLASSATA. \n GAME OVER ---");
        }

        return resultLog.toString();
    }


    public Player getPlayer() { return player; }
    public Shadow getCurrentShadow() { return currentShadow; }
    public int getRoomLevel() { return roomLevel; }
}