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

class RogueTest {
    private class DummyBoard implements GameBoard {
        Player player;
        List<Enemy> monsters = new ArrayList<>();
        public DummyBoard(Player player) {
            this.player = player;
            monsters.add(new Monster('s', "Monster",40,2,3,30,3).init(pos.down(),mcb,this));
            monsters.add(new Monster('q', "Monster2",40,2,3,30,3).init(pos.right(),mcb,this));
        }
        @Override public boolean inBounds(Game.Utils.Position position) { return true; }
        @Override public void swapPositions(Tile tile1, Tile tile2) { }
        @Override public Tile removeEnemy(Enemy enemy) {
            monsters.remove(enemy);
            return new Empty().init(enemy.getPosition());
        }
        @Override public Player getPlayer() { return player; }
        @Override public List<Enemy> getEnemies() { return monsters; }
        @Override public void render() { }
        @Override public Tile getTile(Game.Utils.Position position) { return null; }
        @Override public void setTile(Position position, Tile tile) { }
    }

    Rogue player;
    Position pos = new Position(0,0);
    DummyBoard db;
    MessageCallback mcb = System.out::println;
    InputQuery query;
    PlayerDeathCallback pdc = () -> System.out.println("Died");

    @BeforeEach
    void setUp() {
        player = new Rogue("player",150,40,2,20);
        db = new DummyBoard(player);
        player.init(pos,mcb,db,query,pdc);
    }

    @Test
    void levelUp() {
        // before
        assertEquals(150,player.getMaxHP());
        assertEquals(40,player.getAttack());
        assertEquals(2,player.getDefense());
        // act
        player.levelUp();
        // after
        assertEquals(2,player.getLevel());
        assertEquals(150 + 10*2, player.getMaxHP());   // +20 HP
        assertEquals(40 + 14,   player.getAttack());   // +14 ATK (4+3)*level
        assertEquals(2  + 2,    player.getDefense()); // +2  DEF
        // energy capacity grew by 100 – validated through description string
        assertTrue(player.description().contains("Energy: 100/200"));
    }

    @Test
    void onTickActions() {
        // Drain all energy (5 casts * 20 = 100)
        for(int i=0;i<5;i++) {
            assertTrue(player.canCastAbility());
            player.castSpecialAbility();
        }
        assertFalse(player.canCastAbility()); // 0 energy
        player.onTickActions();               // +10 energy -> still < cost
        assertFalse(player.canCastAbility());
        player.onTickActions();               // +10 energy (total 20) -> can cast again
        assertTrue(player.canCastAbility());
    }

    @Test
    void canCastAbility() {
        assertTrue(player.canCastAbility());
        // Exhaust energy
        for(int i=0;i<5;i++)
            player.castSpecialAbility();
        assertFalse(player.canCastAbility());
        // Regenerate exactly once (10 energy) – still false
        player.onTickActions();
        assertFalse(player.canCastAbility());
        // Regenerate second time (20 energy) – now true
        player.onTickActions();
        assertTrue(player.canCastAbility());
    }

    @Test
    void castSpecialAbility() {
        // Cast‑tick‑tick loop ensures we never run out of energy completely
        for(int i=0;i<10;i++) {
            assertDoesNotThrow(() -> player.castSpecialAbility());
            // two ticks to recharge 20 energy (ability cost)
            player.onTickActions();
            player.onTickActions();
        }
    }

    @Test
    void description() {
        String desc = player.description();
        assertTrue(desc.contains("Energy:"));
        assertTrue(desc.contains("Attack: 40"));
    }
}
