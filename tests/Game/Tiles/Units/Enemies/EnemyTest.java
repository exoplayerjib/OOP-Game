package Game.Tiles.Units.Enemies;

import Game.Board.Board;
import Game.Callbacks.MessageCallback;
import Game.Callbacks.PlayerDeathCallback;
import Game.Tiles.Units.Players.Player;
import Game.Tiles.Units.Players.Rogue;
import View.Parser.TileFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class EnemyTest {
    Path levelPath = Paths.get("tests", "TestUtils", "testlevel.txt");

    Board board;
    Player player;
    TileFactory tf = new TileFactory();

    MessageCallback mcb = System.out::println;
    PlayerDeathCallback pdc = () -> System.out.println("Died");

    Monster monster;
    Trap trap;

    @BeforeEach
    void setUp() throws Exception {
        player = new Rogue("rouge",100,5,5,20);
        board = new Board(levelPath,player,tf,mcb,() -> Player.Actions.STAY,pdc);
        monster = new Monster('m',"monster", 200,40,30,40,5);
        monster.init(player.getPosition().right().up().right(),mcb,board);
        board.setTile(monster.getPosition(),monster);
        board.getEnemies().add(monster);
        trap = new Trap('t',"trap",100,50,40,20,2,4);
        trap.init(player.getPosition().right().up(),mcb,board);
        board.setTile(trap.getPosition(),trap);
        board.getEnemies().add(trap);
    }

    @Test
    void getExperienceValue() {
        assertEquals(40,monster.getExperienceValue());
        assertEquals(20,trap.getExperienceValue());
    }

    @Test
    void visit() {
        int oldHealth = player.getCurrentHP();
        while (player.getCurrentHP() == oldHealth){
            trap.visit(player);
        }
        assertNotEquals(oldHealth,player.getCurrentHP());
        oldHealth = player.getCurrentHP();
        while (player.getCurrentHP() == oldHealth){
            monster.visit(player);
        }
        assertNotEquals(oldHealth,player.getCurrentHP());

        //movement visit usage were tested in takeTurn test in Monster
    }
}