package Game.Tiles.Units.Enemies;

import Game.Board.Board;
import Game.Callbacks.MessageCallback;
import Game.Callbacks.PlayerDeathCallback;
import Game.Tiles.BoardParts.Empty;
import Game.Tiles.Units.Players.Player;
import Game.Tiles.Units.Players.Rogue;
import View.Parser.TileFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class TrapTest {
    Path levelPath = Paths.get("tests", "TestUtils", "testlevel.txt");

    Board board;
    Player player;
    TileFactory tf = new TileFactory();

    MessageCallback mcb = System.out::println;
    PlayerDeathCallback pdc = () -> System.out.println("Died");
    Trap trap;

    @BeforeEach
    void setUp() throws IOException {
        player = new Rogue("rouge",100,5,5,20);
        board = new Board(levelPath,player,tf,mcb,() -> Player.Actions.STAY,pdc);
        trap = new Trap('t',"trap",100,50,40,20,2,4);
        trap.init(player.getPosition().right().up(),mcb,board);
        board.setTile(trap.getPosition(),trap);
        board.getEnemies().add(trap);
    }

    @Test
    void takeTurn() {
        int health = player.getCurrentHP();
        while (health == player.getCurrentHP()){
            trap.takeTurn();
        }
        assertNotEquals(health,player.getCurrentHP());

        board.setTile(trap.getPosition(),board.removeEnemy(trap));

        health = player.getCurrentHP();
        trap = new Trap('t',"trap",100,50,40,20,2,4);
        trap.init(player.getPosition().right().right().right(),mcb,board); //too far to attack
        trap.takeTurn();
        assertEquals(health,player.getCurrentHP());
    }

    @Test
    void onTick() {
        assertEquals("t",trap.toString());
        for (int i =0; i<3; i++)
            trap.onTick();
        assertEquals(".",trap.toString());
        for (int i =0; i<5; i++)
            trap.onTick();
        assertEquals("t",trap.toString());
    }

    @Test
    void isVisible() {
        assertEquals(true,trap.isVisible());
        for (int i =0; i<3; i++)
            trap.onTick();
        assertEquals(false,trap.isVisible());
    }
}