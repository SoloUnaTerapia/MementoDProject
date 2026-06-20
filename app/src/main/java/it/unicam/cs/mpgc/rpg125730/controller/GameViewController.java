package it.unicam.cs.mpgc.rpg125730.controller;

import it.unicam.cs.mpgc.rpg125730.model.Element;
import it.unicam.cs.mpgc.rpg125730.model.Player;
import it.unicam.cs.mpgc.rpg125730.model.Shadow;
import it.unicam.cs.mpgc.rpg125730.service.CombatManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

public class GameViewController {

    private final CombatManager combatManager;
    private Player player;
    private Shadow currentShadow;
    private int roomLevel = 1;

    @FXML private Label roomLabel;
    @FXML private Label playerStatsLabel;
    @FXML private ProgressBar playerSanityBar;
    @FXML private ProgressBar playerFocusBar;
    @FXML private Label enemyStatsLabel;
    @FXML private ProgressBar enemySanityBar;
    @FXML private TextArea combatLogArea;

    public GameViewController(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @FXML
    public void initialize() {
        startNewGame();
    }

    private void startNewGame() {
        this.player = new Player("Diver", 100, 50);
        this.roomLevel = 1;
        spawnEnemy();
        updateUI();
        combatLogArea.setText("Immersione nel Memento... Stanza " + roomLevel + "\n");
    }

    private void spawnEnemy() {  //per prova 1 nemico solo fisso
        this.currentShadow = new Shadow("Pixie", 40 + (roomLevel * 5), 8 + roomLevel, Element.FIRE, Element.ICE);
    }

    // --- AZIONI BOTTONI ---

    @FXML
    public void onPhysicalAttack() {
        String log = combatManager.executePhysicalAttack(player, currentShadow, 15);
        player.recoverFocus(10); //se il player fa attacco fisico gli faccio recuperare un po di focus
        processTurn(log, true); //attacco fisico sempre a buin fine
    }

    @FXML
    public void onFireSkill() {
        int cost = 10;
        if (player.getFocus() < cost) {
            combatLogArea.appendText("\nNon hai abbastanza Focus per usare Skill Fuoco!\n");
            return; //non faccio andare avanti il turno
        }
        String log = combatManager.executeElementalAttack(player, currentShadow, Element.FIRE, cost, 20);
        processTurn(log, true);
    }

    @FXML
    public void onIceSkill() {
        int cost = 10;
        if (player.getFocus() < cost) {
            combatLogArea.appendText("\nNon hai abbastanza Focus per usare Skill Ghiaccio!\n");
            return; //non faccio andare avanti il turno
        }
        String log = combatManager.executeElementalAttack(player, currentShadow, Element.ICE, cost, 20);
        processTurn(log, true);
    }

    @FXML
    public void onHeal() {
        int cost = 15;
        if (player.getFocus() < cost) {
            combatLogArea.appendText("\nNon hai abbastanza Focus per stabilizzarti\n");
            return; //non faccio andare avanti il turno
        }
        String log = combatManager.executeStabilize(player, cost, 30+roomLevel);
        processTurn(log, true);
    }

    // --- GESTIONE TURNO E NEMICO ---

    private void processTurn(String playerActionLog, boolean enemyMustAttack) {
        combatLogArea.appendText("\n" + playerActionLog + "\n");

        if (!currentShadow.isConscious()) {
            showAlert("Vittoria", "Hai sconfitto l'Ombra! Recuperi Sanity e Focus. Avanzi alla stanza successiva.");

            //ogni livello ricarica il focus
            player.recoverFocus(20+roomLevel);


            roomLevel++;
            spawnEnemy();
            combatLogArea.appendText("\n--- Entri nella Stanza " + roomLevel + " ---\n");
            updateUI();
            return;
        }

        if (enemyMustAttack) {
            String enemyLog = combatManager.executeEnemyTurn(currentShadow, player);
            combatLogArea.appendText(enemyLog + "\n");

            if (!player.isConscious()) {
                showAlert("Game Over", "Morte cerebrale");
                startNewGame();
                return;
            }
        }

        updateUI();
    }

    private void updateUI() {
        roomLabel.setText("Stanza: " + roomLevel);

        playerStatsLabel.setText(player.getName() + " - Sanity: " + player.getSanity() + " | Focus: " + player.getFocus());
        playerSanityBar.setProgress((double) player.getSanity() / player.getMaxSanity());
        playerFocusBar.setProgress((double) player.getFocus() / player.getMaxFocus());

        enemyStatsLabel.setText(currentShadow.getName() + " - Sanity: " + currentShadow.getSanity());
        enemySanityBar.setProgress((double) currentShadow.getSanity() / currentShadow.getMaxSanity());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}