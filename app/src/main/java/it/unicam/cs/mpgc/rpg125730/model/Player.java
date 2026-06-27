package it.unicam.cs.mpgc.rpg125730.model;
/**
 * Rappresenta l'entità controllata dall'utente nel dominio del gioco.
 * La sua responsabilità esclusiva è mantenere la coerenza del proprio stato vitale
 * (Sanity) ed energetico (Focus), impedendo valori invalidi
 */
public class Player implements CombatEntity {
    private final String name;
    private int sanity;
    private int maxSanity;
    private int focus;
    private int maxFocus;

    public Player(String name, int maxSanity, int maxFocus) {
        this.name = name;
        this.maxSanity = maxSanity;
        this.sanity = maxSanity;
        this.maxFocus = maxFocus;
        this.focus = maxFocus;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getSanity() { return sanity; }

    @Override
    public int getMaxSanity() { return maxSanity; }


    public int getFocus() { return focus; }


    public int getMaxFocus() { return maxFocus; }

    @Override
    public void takeDamage(int amount) {
        if (amount < 0) return;
        this.sanity -= amount;
        if (this.sanity < 0) this.sanity = 0;
    }

    public void consumeFocus(int amount) {
        if (amount > 0 && this.focus >= amount) {
            this.focus -= amount;
        }
    }
    public void recoverFocus(int amount) {
        if (amount < 0) return;
        this.focus += amount;
        if (this.focus > maxFocus) {
            this.focus = maxFocus;
        }
    }
    @Override
    public void recoverSanity(int amount) {
        if (amount < 0) return;
        this.sanity += amount;
        if (this.sanity > maxSanity) this.sanity = maxSanity;
    }


    @Override
    public boolean isConscious() {
        return this.sanity > 0;
    }

    public void levelUp(int sanityBonus, int focusBonus) {
        this.maxSanity += sanityBonus;
        this.maxFocus += focusBonus;

        this.sanity += sanityBonus;
        this.focus += focusBonus;
    }
}
