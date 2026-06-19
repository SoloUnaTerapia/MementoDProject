package it.unicam.cs.mpgc.rpg125730.model;

public class Shadow implements CombatEntity {
    private final String name;
    private int sanity;
    private final int maxSanity;
    private int focus;
    private final int maxFocus;

    private final int baseDamage;
    private final Element weakness;
    private final Element resistance;

    public Shadow(String name, int maxSanity, int maxFocus, int baseDamage, Element weakness, Element resistance) {
        this.name = name;
        this.maxSanity = maxSanity;
        this.sanity = maxSanity;
        this.maxFocus = maxFocus;
        this.focus = maxFocus;
        this.baseDamage = baseDamage;
        this.weakness = weakness;
        this.resistance = resistance;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getSanity() { return sanity; }

    @Override
    public int getMaxSanity() { return maxSanity; }

    @Override
    public int getFocus() { return focus; }

    @Override
    public int getMaxFocus() { return maxFocus; }

    public int getBaseDamage() { return baseDamage; }

    public Element getWeakness() { return weakness; }

    public Element getResistance() { return resistance; }

    @Override
    public void takeDamage(int amount) {
        if (amount < 0) return;
        this.sanity -= amount;
        if (this.sanity < 0) this.sanity = 0;
    }

    @Override
    public void consumeFocus(int amount) {
        if (amount > 0 && this.focus >= amount) {
            this.focus -= amount;
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