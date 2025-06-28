package Game.Tiles.Units.Players;

import Game.Board.GameBoard;
import Game.Callbacks.MessageCallback;
import Game.Callbacks.PlayerDeathCallback;
import Game.Tiles.BoardParts.Empty;
import Game.Tiles.Tile;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Enemies.Monster;
import Game.Utils.Position;
import View.Input.InputQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MageTest {
    // ------------------------------- Dummy board ------------------------------- //
    private class DummyBoard implements GameBoard {
        Player player;
        List<Enemy> monsters = new ArrayList<>();
        public DummyBoard(Player player) {
            this.player = player;
            monsters.add(new Monster('s', "Monster",40,2,3,30,3).init(pos.down(),mcb,this));
            monsters.add(new Monster('q', "Monster2",40,2,3,30,3).init(pos.right(),mcb,this));
        }
        @Override public boolean inBounds(Position position) { return true; }
        @Override public void swapPositions(Tile tile1, Tile tile2) { }
        @Override public Tile removeEnemy(Enemy enemy) {
            monsters.remove(enemy);
            return new Empty().init(enemy.getPosition());
        }
        @Override public Player getPlayer() { return player; }
        @Override public List<Enemy> getEnemies() { return monsters; }
        @Override public void render() { }
        @Override public Tile getTile(Position position) { return null; }
        @Override public void setTile(Position position, Tile tile) { }
    }

    // ------------------------------- Test members ------------------------------ //
    Mage player;
    Position pos = new Position(0,0);
    DummyBoard db;
    MessageCallback mcb = System.out::println;
    InputQuery query;
    PlayerDeathCallback pdc = () -> System.out.println("Died");

    @BeforeEach
    void setUp() {
        //        name   HP  ATK DEF manaPool cost spellPower hits range
        player = new Mage("player",100,5,1,300,30,15,5,6);
        db = new DummyBoard(player);
        player.init(pos,mcb,db,query,pdc);
    }

    // ---------------------------------- Tests ---------------------------------- //
    @Test
    void levelUp() {
        // before
        assertEquals(100, player.getMaxHP());
        assertEquals(5, player.getAttack());
        assertEquals(1, player.getDefense());
        // act
        player.levelUp();
        // after -> base gains + mage‑specific gains (mana + spell power)
        assertEquals(2, player.getLevel());
        assertEquals(120, player.getMaxHP());              // +20 HP (10*lvl)
        assertEquals(13, player.getAttack());              // +8  ATK (4*lvl)
        assertEquals(3,  player.getDefense());             // +2  DEF (1*lvl)
        // mana pool up by 25*lvl = 50, spell power +10*lvl = +20
        assertTrue(player.description().contains("Mana: 162/350"));
        assertTrue(player.description().contains("Spell Power: 35"));
    }

    @Test
    void onTickActions() {
        for (Enemy e : db.getEnemies()) //clearing the enemyList so the player wont get levelup mana
            db.removeEnemy(e);
        // Drain mana with two casts (75 -> 45 -> 15)
        for(int i=0;i<2;i++) {
            assertTrue(player.canCastAbility());
            player.castSpecialAbility();
        }
        assertFalse(player.canCastAbility());   // 15/300 mana – insufficient
        // Tick 14 times (regen 14 mana) – still below cost
        for(int i=0;i<14;i++) {
            player.onTickActions();
        }
        assertFalse(player.canCastAbility());   // 29 mana
        player.onTickActions();                 // +1 mana (total 30) -> can cast
        assertTrue(player.canCastAbility());
    }

    @Test
    void canCastAbility() {
        for (Enemy e : db.getEnemies()) //clearing the enemyList so the player wont get levelup mana
            db.removeEnemy(e);

        assertTrue(player.canCastAbility());
        // Exhaust mana (two casts)
        player.castSpecialAbility();
        player.castSpecialAbility();
        assertFalse(player.canCastAbility());
        // Regenerate 15 mana exactly (level=1 → +1 per tick)
        for(int i=0;i<15;i++)
            player.onTickActions();
        assertTrue(player.canCastAbility());
    }

    @Test
    void castSpecialAbility() {
        // Cast‑tick loop: 30 ticks regenerate full cost (30 mana)
        for(int i=0;i<8;i++) {
            assertDoesNotThrow(() -> player.castSpecialAbility());
            for(int t=0;t<30;t++)
                player.onTickActions();
        }
    }

    @Test
    void description() {
        String desc = player.description();
        assertTrue(desc.contains("Mana:"));
        assertTrue(desc.contains("Spell Power:"));
    }
}
