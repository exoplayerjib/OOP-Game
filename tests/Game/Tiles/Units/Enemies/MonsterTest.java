package Game.Tiles.Units.Enemies;

import Game.Board.Board;
import Game.Callbacks.MessageCallback;
import Game.Callbacks.PlayerDeathCallback;
import Game.Tiles.Units.Players.Player;
import Game.Tiles.Units.Players.Rogue;
import Game.Utils.Position;
import View.Parser.TileFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {
    Path levelPath = Paths.get("tests", "TestUtils", "testlevel.txt");

    Board board;
    Player player;
    TileFactory tf = new TileFactory();

    MessageCallback mcb = System.out::println;
    PlayerDeathCallback pdc = () -> System.out.println("Died");

    Monster monster;

    @BeforeEach
    void setUp() throws IOException {
        player = new Rogue("rouge",100,5,5,20);
        board = new Board(levelPath,player,tf,mcb,() -> Player.Actions.STAY,pdc);
        monster = new Monster('m',"monster", 200,40,30,40,5);
        monster.init(player.getPosition().right().up().right(),mcb,board);
        board.setTile(monster.getPosition(),monster);
        board.getEnemies().add(monster);
    }

    @Test
    void takeTurn() {
        //should chase and attack the player
        int health = player.getCurrentHP();
        Position old = monster.getPosition();
        while (health == player.getCurrentHP()){
            monster.takeTurn();
        }
        assertNotEquals(health,player.getCurrentHP());
        assertNotEquals(old,monster.getPosition());

        board.setTile(monster.getPosition(),board.removeEnemy(monster));
        monster = new Monster('m',"monster", 200,40,30,40,2);
        monster.init(player.getPosition().right().up().up().right().right(),mcb,board);
        board.setTile(monster.getPosition(),monster);
        board.getEnemies().add(monster);

        //should walk randomly player is not visible to him
        for (int i = 0; i<3;i++){
            monster.takeTurn();
            assertNotEquals(player.getPosition(),monster.getPosition());
        }
    }

    @Test
    void tryMove() {
        Position old = monster.getPosition();
        monster.tryMove(Monster.Directions.UP);
        assertEquals(old.up(),monster.getPosition());
        old = monster.getPosition();
        monster.tryMove(Monster.Directions.DOWN);
        assertEquals(old.down(),monster.getPosition());
        old = monster.getPosition();
        monster.tryMove(Monster.Directions.LEFT);
        assertEquals(old.left(),monster.getPosition());
        old = monster.getPosition();
        monster.tryMove(Monster.Directions.RIGHT);
        assertEquals(old.right(),monster.getPosition());
    }
}