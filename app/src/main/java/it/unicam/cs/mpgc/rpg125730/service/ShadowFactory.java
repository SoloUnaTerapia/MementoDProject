package it.unicam.cs.mpgc.rpg125730.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg125730.dto.ShadowDataDTO;
import it.unicam.cs.mpgc.rpg125730.model.Shadow;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

/**
 * Factory responsabile della generazione dei nemici (ombre)
 * Carica il catalogo delle ombre da un file JSON e ne estrae una casualmente,
 * scalando le sue statistiche in base al livello della stanza
 */
public class ShadowFactory {

    private final List<ShadowDataDTO> database;
    private final Random random;

    public ShadowFactory() {
        this.random = new Random();
        this.database = loadDatabase();
    }

    /**
     * Legge il file JSON e lo converte in DTO
     */
    private List<ShadowDataDTO> loadDatabase() {
        try (Reader reader = new InputStreamReader(
                getClass().getResourceAsStream("/shadowDatabase.json"))) {

            Gson gson = new Gson();
            Type listType = new TypeToken<List<ShadowDataDTO>>() {}.getType();
            return gson.fromJson(reader, listType);

        } catch (Exception e) {
            System.err.println("Errore nel caricamento del database mostri. Uso fallback.");
            //fallback d'emergenza se il file non viene trovato
            return List.of(new ShadowDataDTO("Ombra Generica", 30, 5, null, null, null));
        }
    }

    /**
     * Estrae un'ombra a caso dal database e ne scala la potenza in base alla stanza
     */
    public Shadow generateRandomShadow(int roomLevel) {

        ShadowDataDTO data = database.stream()
                .skip(random.nextInt(database.size()))
                .findFirst()
                .orElse(database.get(0));

        //scalo i danni e gli HP in base alla stanza per rendere il gioco progressivamente più difficile
        int scaledHp= data.baseHp()+(roomLevel * 5);
        int scaledDamage = data.baseDamage()+ roomLevel;

        return new Shadow(data.name(), scaledHp, scaledDamage, data.weakness(), data.resistance(), data.imagePath());
    }

    /**
     * Cerca un'ombra specifica per nome nel database e la scala in base alla stanza.
     * Utilizzato durante il caricamento della partita.
     */
    public Shadow getShadowByName(String name, int roomLevel) {
        ShadowDataDTO data = database.stream()
                .filter(d -> d.name().equals(name)) // Cerca il nome esatto
                .findFirst()
                .orElse(database.get(0)); // Se per assurdo non lo trova, prende il primo

        int scaledHp = data.baseHp() + (roomLevel * 5);
        int scaledDamage = data.baseDamage() + roomLevel;

        return new Shadow(data.name(), scaledHp, scaledDamage, data.weakness(), data.resistance(), data.imagePath());
    }
}