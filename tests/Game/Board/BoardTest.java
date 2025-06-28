package Game.Board;

import Game.Tiles.BoardParts.Wall;
import Game.Tiles.Tile;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Enemies.Monster;
import Game.Tiles.Units.Players.Player;
import Game.Tiles.Units.Players.Warrior;
import Game.Utils.Position;
import View.Parser.TileFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Path levelPath = Paths.get("tests", "TestUtils", "testlevel.txt");
    Player player;
    TileFactory tf = new TileFactory();
    Board board;

    @BeforeEach
    void setUp() throws IOException {
        player = new Warrior("warrior", 100, 5, 5, 20);
        board = new Board(levelPath,player,tf,System.out::println,() -> Player.Actions.STAY,() -> System.out.println("Died"));
    }

    @Test
    void inBounds() {
        Position out = new Position(100,100);
        Position in = new Position(5,5);
        assertTrue(board.inBounds(in));
        assertFalse(board.inBounds(out));
    }

    @Test
    void getTile() {
        Tile wall = board.getTile(new Position(0,0));
        assertEquals("#", wall.toString() );
        Tile empty = board.getTile(new Position(1,1));
        assertEquals(".",empty.toString() );
        Tile player1 = board.getTile(player.getPosition());
        assertEquals("@",player1.toString() );
    }

    @Test
    void setTile() {
        Wall wall = new Wall();
        wall.init(new Position(1,1));
        board.setTile(wall.getPosition(),wall);
        assertEquals("#",board.getTile(new Position(1,1)).toString());
    }

    @Test
    void swapPositions() {
        Wall wall = new Wall();
        wall.init(new Position(3,2));
        board.setTile(wall.getPosition(),wall);
        board.swapPositions(wall,board.getTile(new Position(4,2)));
        assertEquals(".",board.getTile(new Position(3,2)).toString());
        assertEquals("#",board.getTile(new Position(4,2)).toString());
    }

    @Test
    void removeEnemy() {
        Enemy e = new Monster('m', "monster", 100, 10, 10, 100,3);
        e.init(new Position(3, 2), System.out::println, board);
        board.setTile(e.getPosition(), e);
        board.getEnemies().add(e);
        //Added the enemy
        assertEquals(1,board.getEnemies().size());
        assertEquals( e.toString(), board.getTile(new Position(3, 2)).toString() );

        board.setTile(e.getPosition(),board.removeEnemy(e));
        assertEquals(0,board.getEnemies().size());
        assertEquals(".", board.getTile(new Position(3, 2)).toString());
    }

    @Test
    void getPlayer() {
        assertEquals(player, board.getPlayer());
    }

    @Test
    void getEnemies() {
        Enemy e = new Monster('m', "monster", 100, 10, 10, 100,2);
        e.init(new Position(1, 1), System.out::println, board);
        board.getEnemies().add(e);
        board.setTile(e.getPosition(), e);
        assertTrue(board.getEnemies().contains(e));
    }
}