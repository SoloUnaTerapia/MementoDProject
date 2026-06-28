package it.unicam.cs.mpgc.rpg125730.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg125730.dto.GameStateDTO;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
/**
 * Implementazione concreta del servizio di persistenza
 * Ha la singola responsabilità di serializzare e deserializzare lo stato di gioco
 * su un file di testo locale in formato JSON,
 * avvalendosi della libreria Gson
 */
public class JsonPersistenceService implements PersistenceService {

    private static final String FILEPATH = "savegame.json";
    private final Gson gson;

    public JsonPersistenceService() {

        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void saveGame(GameStateDTO state) throws Exception {
        try (Writer writer = new FileWriter(FILEPATH)) {
            gson.toJson(state, writer);
        }
    }

    @Override
    public GameStateDTO loadGame() throws Exception {
        if (!Files.exists(Path.of(FILEPATH))) {
            throw new Exception("Nessun file di salvataggio trovato");
        }
        try (Reader reader = new FileReader(FILEPATH)) {
            return gson.fromJson(reader, GameStateDTO.class);
        }
    }
}