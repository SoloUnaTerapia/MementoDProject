package it.unicam.cs.mpgc.rpg125730.model;
/**
 * Definisce le funzionalità e i comportamenti di un generico combattente nel sistema di gioco.
 */
public interface CombatEntity {
    String getName();
    int getSanity();
    int getMaxSanity();
    int getFocus();
    int getMaxFocus();
    void takeDamage(int amount);
    void consumeFocus(int amount); //consuma i punti focus necessari per eseguire una mossa abilità
    void recoverSanity(int amount);
    boolean isConscious(); //restituisce true se l'entità è ancora viva
}