package it.unicam.cs.mpgc.rpg125730;

import it.unicam.cs.mpgc.rpg125730.model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class PlayerTest {
    @Test
    void sanityShouldNeverGoBelowZero() {
        Player player = new Player("Test", 100, 50);

        //infliggo un danno enorme
        player.takeDamage(500);

        //verifica che la vita si fermi a 0 e non vada in negativo
        assertEquals(0, player.getSanity());
        assertFalse(player.isConscious());
    }

    @Test
    void sanityShouldNeverExceedMax() {
        Player player = new Player("Test", 100, 50);

        //infligge 20 danni (vita: 80/100)
        player.takeDamage(20);

        //si cura di 500 (dovrebbe fermarsi a 100)
        player.recoverSanity(500);

        assertEquals(100, player.getSanity());
    }
}
