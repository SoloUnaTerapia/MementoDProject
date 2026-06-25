package it.unicam.cs.mpgc.rpg125730;

import it.unicam.cs.mpgc.rpg125730.model.Element;
import it.unicam.cs.mpgc.rpg125730.model.Player;
import it.unicam.cs.mpgc.rpg125730.model.Shadow;
import it.unicam.cs.mpgc.rpg125730.service.CombatManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitari per verificare la corretta applicazione delle regole di business
 * del CombatManager (Debolezze, Resistenze e consumi di Focus)
 */
class CombatManagerTest {

    private CombatManager combatManager;
    private Player testPlayer;
    private Shadow testEnemy;

    @BeforeEach
    void setUp() {
        //viene eseguito prima di ogni test per azzerare i dati
        combatManager = new CombatManager();
        testPlayer = new Player("Tester", 100, 50);

        //nemico debole al FUOCO e resistente al GHIACCIO
        testEnemy = new Shadow("Target", 100, 10, Element.FIRE, Element.ICE, "img.png");
    }

    @Test
    void elementalAttackShouldDoubleDamageOnWeakness() {
        int startingSanity = testEnemy.getSanity();
        int baseDamage = 20;

        //esegue attacco Fuoco contro nemico debole al Fuoco
        combatManager.executeElementalAttack(testPlayer, testEnemy, Element.FIRE, 10, baseDamage);

        //verifica: il danno deve essere il doppio (40), quindi la Sanity finale deve essere 60
        assertEquals(startingSanity - 40, testEnemy.getSanity(), "Il danno dovrebbe essere raddoppiato per la debolezza.");
    }

    @Test
    void elementalAttackShouldHalveDamageOnResistance() {
        int startingSanity = testEnemy.getSanity();
        int baseDamage = 20;

        //esegue attacco Ghiaccio contro nemico resistente al Ghiaccio
        combatManager.executeElementalAttack(testPlayer, testEnemy, Element.ICE, 10, baseDamage);

        //verifica: il danno deve essere dimezzato (10), quindi la Sanity finale deve essere 90
        assertEquals(startingSanity - 10, testEnemy.getSanity(), "Il danno dovrebbe essere dimezzato per la resistenza");
    }

    @Test
    void physicalAttackShouldNotConsumeFocus() {
        int startingFocus = testPlayer.getFocus();

        //esegue un attacco fisico
        combatManager.executePhysicalAttack(testPlayer, testEnemy, 15);

        //verifica che il Focus sia rimasto intatto
        assertEquals(startingFocus, testPlayer.getFocus(), "L'attacco fisico non deve consumare Focus");
    }

    @Test
    void stabilizeShouldFailIfNotEnoughFocus() {
        //consumiamo tutto il focus per portarlo a zero
        testPlayer.consumeFocus(50);

        //proviamo a curarci (che costa 15)
        String log = combatManager.executeStabilize(testPlayer, 15, 30);

        //verifica che la mossa sia fallita
        assertTrue(log.contains("Non hai abbastanza Focus"), "Dovrebbe fallire se il Focus è insufficiente");
    }
}