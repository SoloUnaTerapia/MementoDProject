package it.unicam.cs.mpgc.rpg125730.model;

public class Player implements CombatEntity {
    private final String name;
    private int sanity;
    private final int maxSanity;
    private int focus;
    private final int maxFocus;

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
}
