package it.unicam.cs.mpgc.rpg125730.controller;

import it.unicam.cs.mpgc.rpg125730.model.Element;
import it.unicam.cs.mpgc.rpg125730.model.Player;
import it.unicam.cs.mpgc.rpg125730.model.Shadow;
import it.unicam.cs.mpgc.rpg125730.persistence.CompendioRepository;
import it.unicam.cs.mpgc.rpg125730.persistence.entity.DiscoveredShadowEntity;
import it.unicam.cs.mpgc.rpg125730.service.CombatManager;
import it.unicam.cs.mpgc.rpg125730.dto.GameStateDTO;
import it.unicam.cs.mpgc.rpg125730.persistence.PersistenceService;
import it.unicam.cs.mpgc.rpg125730.service.ShadowFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class GameViewController {

    private final CombatManager combatManager;
    private final PersistenceService persistenceService;
    private final ShadowFactory shadowFactory;
    private Player player;
    private Shadow currentShadow;
    private int roomLevel = 1;
    private final CompendioRepository compendioRepository;

    @FXML private Label roomLabel;
    @FXML private Label playerStatsLabel;
    @FXML private ProgressBar playerSanityBar;
    @FXML private ProgressBar playerFocusBar;
    @FXML private Label enemyStatsLabel;
    @FXML private ProgressBar enemySanityBar;
    @FXML private TextArea combatLogArea;
    @FXML private javafx.scene.layout.HBox combatButtonsBox;
    @FXML private ImageView playerImageView;
    @FXML private ImageView enemyImageView;

    public GameViewController(CombatManager combatManager, PersistenceService persistenceService, CompendioRepository compendioRepository) {
        this.combatManager = combatManager;
        this.persistenceService = persistenceService;
        this.shadowFactory = new ShadowFactory();
        this.compendioRepository = compendioRepository;
    }

    @FXML
    public void initialize() {
        startNewGame();
    }

    private void startNewGame() {
        this.player = new Player("Joker", 100, 50);
        this.roomLevel = 1;
        spawnEnemy();
        playerImageView.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/images/Joker.png")));
        updateUI();
        if (combatButtonsBox != null) {
            combatButtonsBox.setDisable(false);
        }

        combatLogArea.setText("Inizio immersione cognitiva... Stanza " + roomLevel + "\n");
    }

    private void spawnEnemy() {  //per prova 1 nemico solo fisso
        this.currentShadow = shadowFactory.generateRandomShadow(roomLevel);
        //registra o aggiorna l'Ombra nel Compendio GLOBALE (Database)
        compendioRepository.registerEncounter(currentShadow);
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
    public void onCognitiveSkill() {
        int cost = 20;
        if (player.getFocus() < cost) {
            combatLogArea.appendText("\nNon hai abbastanza Focus per il Sovraccarico Cognitivo!\n");
            return;
        }
       
        String log = combatManager.executeElementalAttack(player, currentShadow, Element.COGNITIVE, cost, 30);
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

        //case Vittoria contro l'Ombra
        if (!currentShadow.isConscious()) {
            combatLogArea.appendText("\n>>> VITTORIA! Hai sconfitto l'Ombra! <<<\n");
            combatLogArea.appendText(">>> Recuperi 15 Focus. Avanzi alla stanza successiva... <<<\n");

            player.recoverFocus(15);
            roomLevel++;
            spawnEnemy();

            combatLogArea.appendText("\n--- Entri nella Stanza " + roomLevel + " ---\n");
            updateUI();
            return; // Se il nemico muore, il turno finisce qui
        }

        //turno del nemico
        if (enemyMustAttack) {
            String enemyLog = combatManager.executeEnemyTurn(currentShadow, player);
            combatLogArea.appendText(enemyLog + "\n");

            //case Sconfitta del giocatore
            if (!player.isConscious()) {
                combatLogArea.appendText("\n######################################\n");
                combatLogArea.appendText("       LA TUA MENTE E' CROLLATA       \n");
                combatLogArea.appendText("              GAME OVER               \n");
                combatLogArea.appendText("######################################\n");
                combatLogArea.appendText("\n-- Clicca su 'Nuova Partita' per riprovare. --\n\n");

                //disabilita i bottoni
                combatButtonsBox.setDisable(true);
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
        String path = "/images/" + currentShadow.getImagePath();
        try {
            enemyImageView.setImage(new Image(getClass().getResourceAsStream(path)));
        } catch (Exception e) {
            System.err.println("Immagine non trovata: " + path);
        }

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
    @FXML
    public void onSaveGame() {
        try {
            //creo il DTO con i parametri attuali
            GameStateDTO state = new GameStateDTO(
                    roomLevel,
                    player.getSanity(),
                    player.getFocus(),
                    currentShadow.getSanity(),
                    currentShadow.getName()
            );
            //passo i dati al servizio
            persistenceService.saveGame(state);
            combatLogArea.appendText("\n[SYSTEM] Partita salvata con successo\n");
        } catch (Exception e) {
            showAlert("Errore di salvataggio", "Impossibile salvare la partita: " + e.getMessage());
        }
    }

    @FXML
    public void onLoadGame() {
        try {

            GameStateDTO state = persistenceService.loadGame();

            //ripristino le variabili
            this.roomLevel = state.roomLevel();
            this.player = new Player("Joker", 100, 50); // Ricreo il player e forzo i valori salvati

            //forzoi danni per far tornare le barre come prima
            int playerDamage=100 - state.playerSanity();
            int playerFocusSpent=50 - state.playerFocus();
            if(playerDamage>0)player.takeDamage(playerDamage);
            if(playerFocusSpent>0)player.consumeFocus(playerFocusSpent);

            //ricreo il nemico
            Shadow temporaryShadow = shadowFactory.generateRandomShadow(roomLevel);

            // 2. Ricreiamo il nemico del salvataggio, ma copiamo le debolezze e la foto da quello generato
            this.currentShadow = new Shadow(
                    state.enemyName(),
                    40 + (roomLevel * 5),
                    8 + roomLevel,
                    temporaryShadow.getWeakness(),
                    temporaryShadow.getResistance(),
                    temporaryShadow.getImagePath()
            );            int enemyDamage = currentShadow.getMaxSanity() - state.enemySanity();
            if(enemyDamage > 0) currentShadow.takeDamage(enemyDamage);

            if (combatButtonsBox != null) {
                combatButtonsBox.setDisable(false);
            }

            updateUI();
            combatLogArea.appendText("\n[SYSTEM] Partita caricata dalla Stanza " + roomLevel + "\n");
        } catch (Exception e) {
            showAlert("Errore", "Nessun salvataggio trovato o file corrotto");
        }
    }

    @FXML
    public void onNewGame() {
        startNewGame();
    }

    @FXML
    public void onOpenCompendium() {
        List<DiscoveredShadowEntity> compendio = compendioRepository.getAllDiscoveredShadows();

        StringBuilder sb = new StringBuilder("\n=== COMPENDIO DELLE OMBRE ===\n");
        for (DiscoveredShadowEntity shadow : compendio) {
            sb.append(String.format("- %s [Debole: %s | Resiste: %s] | Incontri: %d\n",
                    shadow.getName(), shadow.getWeakness(), shadow.getResistance(), shadow.getEncounterCount()));
        }
        sb.append("=============================\n");

        combatLogArea.appendText(sb.toString());
    }

}