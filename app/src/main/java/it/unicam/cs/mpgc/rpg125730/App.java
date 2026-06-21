package it.unicam.cs.mpgc.rpg125730;

import it.unicam.cs.mpgc.rpg125730.controller.GameViewController;
import it.unicam.cs.mpgc.rpg125730.persistence.JsonPersistenceService;
import it.unicam.cs.mpgc.rpg125730.persistence.PersistenceService;
import it.unicam.cs.mpgc.rpg125730.service.CombatManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Inizializzo il Service
        CombatManager combatManager = new CombatManager();
        PersistenceService persistenceService =
                new JsonPersistenceService();

        // 2. Carico l'FXML
        URL fxmlUrl = getClass().getResource("/it/unicam/cs/mpgc/rpg125730/view/gameView.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl);


        loader.setControllerFactory(type -> {
            if (type == GameViewController.class) {
                return new GameViewController(combatManager, persistenceService);
            }
            throw new IllegalArgumentException("Controller non previsto: " + type);
        });

        // 4. Mostro la scena
        Scene scene = new Scene(loader.load(), 800, 600);
        primaryStage.setTitle("Memento - Cognitive RPG");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}