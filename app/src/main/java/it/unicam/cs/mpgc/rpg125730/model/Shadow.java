package it.unicam.cs.mpgc.rpg125730.model;
/**
 * Rappresenta un'entità nemica
 * Oltre a gestire il proprio stato vitale, incapsula le informazioni relative
 * al danno base e alle affinità elementali (debolezze e resistenze) utilizzate
 * dal motore di combattimento
 */
public class Shadow implements CombatEntity {
    private final String name;
    private int sanity;
    private final int maxSanity;

    private final int baseDamage;
    private final Element weakness;
    private final Element resistance;
    private final String imagePath;

    public Shadow(String name, int maxSanity, int baseDamage, Element weakness, Element resistance, String imagePath) {
        this.name = name;
        this.maxSanity = maxSanity;
        this.sanity = maxSanity;
        this.baseDamage = baseDamage;
        this.weakness = weakness;
        this.resistance = resistance;
        this.imagePath = imagePath;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getSanity() { return sanity; }

    @Override
    public int getMaxSanity() { return maxSanity; }


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
    public void recoverSanity(int amount) {
        if (amount < 0) return;
        this.sanity += amount;
        if (this.sanity > maxSanity) this.sanity = maxSanity;
    }

    @Override
    public boolean isConscious() {
        return this.sanity > 0;
    }

    public String getImagePath() { return imagePath; }
}