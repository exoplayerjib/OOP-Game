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

class WarriorTest {

    private class DummyBoard implements GameBoard {
        Player player;
        List<Enemy> monsters = new ArrayList<>();
        public DummyBoard(Player player) { this.player = player;
        monsters.add(new Monster('s', "Monster",40,2,3,30,3).init(pos.down(),mcb,this));
        monsters.add(new Monster('q', "Monster2",40,2,3,30,3).init(pos.right(),mcb,this));
        }
        @Override public boolean inBounds(Game.Utils.Position position) {
            return true;
        }
        @Override public void swapPositions(Game.Tiles.Tile tile1, Game.Tiles.Tile tile2) {
        }
        @Override public Tile removeEnemy(Enemy enemy) {
            monsters.remove(enemy);
            return new Empty().init(enemy.getPosition());
        }
        @Override public Player getPlayer() {
            return null;
        }
        @Override public List<Enemy> getEnemies() {
            return monsters;
        }
        @Override public void render() {

        }
        @Override public Tile getTile(Game.Utils.Position position) {
            return null;
        }
        @Override public void setTile(Position position, Tile tile) {

        }
    }

    Warrior player;
    Position pos = new Position(0,0);
    DummyBoard db;
    MessageCallback mcb = System.out::println;
    InputQuery query;
    PlayerDeathCallback pdc = () -> System.out.println("Died");

    @BeforeEach
    void setUp() {
        player = new Warrior("player",300,30,4,3);
        db = new DummyBoard(player);
        player.init(pos,mcb,db,query,pdc);

    }

    @Test
    void levelUp() {
        assertEquals(300,player.getMaxHP());
        assertEquals(30,player.getAttack());
        assertEquals(4,player.getDefense());
        player.levelUp();
        assertEquals(2,player.getLevel());
        assertEquals(300+15*2,player.getMaxHP());
        assertEquals(30+6*2,player.getAttack());
        assertEquals(4+2*2,player.getDefense());
    }

    @Test
    void onTickActions() {
        assertEquals(0,player.getRemainingCooldown());
        assertEquals(3,player.getAbilityCooldown());
        player.castSpecialAbility();
        for (int j = 3; j > 0; j--) {
            assertEquals(j,player.getRemainingCooldown());
            player.onTickActions();
        }
        assertEquals(0,player.getRemainingCooldown());
    }

    @Test
    void canCastAbility() {
        assertEquals(true,player.canCastAbility());
        player.castSpecialAbility();
        assertEquals(false,player.canCastAbility());
        for (int j = 0; j < 3; j++)
            player.onTickActions();
        assertEquals(true,player.canCastAbility());
    }

    @Test
    void castSpecialAbility() {
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow( () -> player.castSpecialAbility() );
            for (int j = 0; j < 3; j++)
                player.onTickActions();
        }
    }
}