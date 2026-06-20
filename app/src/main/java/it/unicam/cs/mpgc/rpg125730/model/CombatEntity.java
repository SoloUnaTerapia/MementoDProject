package it.unicam.cs.mpgc.rpg125730.model;
/**
 * Definisce le funzionalità e i comportamenti di un generico combattente nel sistema di gioco.
 */
public interface CombatEntity {
    String getName();
    int getSanity();
    int getMaxSanity();

    void takeDamage(int amount);
    void recoverSanity(int amount);
    boolean isConscious();
}