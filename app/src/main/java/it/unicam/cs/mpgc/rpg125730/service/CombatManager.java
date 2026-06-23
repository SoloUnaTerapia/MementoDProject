package it.unicam.cs.mpgc.rpg125730.service;

import it.unicam.cs.mpgc.rpg125730.model.*;

/**
 * Gestisce la logica di combattimento l'applicazione delle regole di gioco
 * e il calcolo dei danni elementali
 */
public class CombatManager {

    /**
     * Esegue un attacco fisico base (non costa Focus, danno fisso)
     */
    public String executePhysicalAttack(CombatEntity attacker, CombatEntity target, int baseDamage) {
        target.takeDamage(baseDamage);
        return attacker.getName() + " sferra un attacco fisico! Infligge " + baseDamage + " danni a " + target.getName() +". "+ attacker.getName()+ " recupera 10 punti focus";
    }

    /**
     * Esegue un attacco cognitivo/elementale, consuma focus e bverifica debolezze/resistenze
     */
    public String executeElementalAttack(Player player, Shadow target, Element attackElement, int focusCost, int baseDamage) {
        //controllo se il focus è sufficiente per fare l'attaco
        if (player.getFocus()<focusCost) {
            return player.getName() + "non ha abbastanza Focus per usare questa abilità";
        }

        player.consumeFocus(focusCost);

        //calcolo dei moltiplicatori (debolezz/resistenza)
        int finalDamage = baseDamage;
        String logMessage = player.getName() + " usa un'abilità di tipo " + attackElement+"\n";

        if (target.getWeakness() == attackElement) {
            finalDamage*=2; // Danno raddoppiato
            logMessage+= "\ncolpo critico, colpita la debolezza ";
        } else if (target.getResistance()==attackElement) {
            finalDamage /= 2; // Danno dimezzato
            logMessage += "\nIl bersaglio resiste all'attacco... ";
        }

        //applicazione del danno
        target.takeDamage(finalDamage);
        logMessage += "Infligge " + finalDamage + " danni a " + target.getName();

        return logMessage;
    }

    /**
     * Azione di cura; consuma focus per ripristinare vita
     */
    public String executeStabilize(Player player, int focusCost, int healAmount) {
        if (player.getFocus() < focusCost) {
            return "Non hai abbastanza Focus per rigenerare salute mentale";
        }

        player.consumeFocus(focusCost);
        player.recoverSanity(healAmount);
        return player.getName() + " ripristina il proprio equilibrio, recuperati " + healAmount + " punti Sanity";
    }

    /**
     * Turno automatico del nemico (Attacco base contro il giocatore)
     */
    public String executeEnemyTurn(Shadow enemy, Player player) {
        if (!enemy.isConscious()) {
            return enemy.getName() + " si dissolve nell'inconscio...";
        }

        int damage = enemy.getBaseDamage();
        player.takeDamage(damage);
        return "L'ombra " + enemy.getName() + " attacca! Infligge " + damage + " punti danno alla sanità mentale di " + player.getName();
    }
}