package Game.Tiles.Units.Players;

import Game.Board.Board;
import Game.Callbacks.MessageCallback;
import Game.Callbacks.PlayerDeathCallback;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Enemies.Monster;
import Game.Utils.Position;
import View.Input.InputQuery;
import View.Parser.TileFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Path levelPath = Paths.get("tests", "TestUtils", "testlevel.txt");
    Warrior warrior;
    Mage mage;
    Rogue rogue;

    Map<Player,Board> playerBoards = new HashMap<>();

    TileFactory tf = new TileFactory();

    List<Player> players = new ArrayList<>();
    MessageCallback mcb = System.out::println;
    PlayerDeathCallback pdc = () -> System.out.println("Died");

    InputQuery stay = ()-> Player.Actions.STAY;
    InputQuery up = ()-> Player.Actions.UP;
    InputQuery down = ()-> Player.Actions.DOWN;
    InputQuery left = ()-> Player.Actions.LEFT;
    InputQuery right = ()-> Player.Actions.RIGHT;
    InputQuery cast = ()-> Player.Actions.CAST;

    private void killPlayer(Player player){
        player.takeDamage(player.getMaxHP());
    }

    private void setBoards(InputQuery iqr){
        try {
            playerBoards.put(warrior, new Board(levelPath,warrior,tf,mcb,iqr,pdc));
            playerBoards.put(mage, new Board(levelPath,mage,tf,mcb,iqr,pdc));
            playerBoards.put(rogue, new Board(levelPath,rogue,tf,mcb,iqr,pdc));
        }
        catch (Exception e) {
            fail("Could not load level");
        }
    }

    private void addEnemyToBoards(Position pos){
        for (Player p : players) {
            Enemy e = new Monster('m',"Monster", 40, 2, 3, 100, 3).init(pos,mcb,playerBoards.get(p));
            playerBoards.get(p).setTile(pos,e);
            playerBoards.get(p).getEnemies().add(e);
        }
    }

    @BeforeEach
    void setUp() {
        warrior = new Warrior("warrior", 300, 30, 4, 3);
        mage = new Mage("mage", 100, 10, 1, 300, 30, 15, 5, 6);
        rogue = new Rogue("rogue", 140,60,4,20);
        players.add(warrior);
        players.add(mage);
        players.add(rogue);
    }

    @Test
    void testDeathToString() {
        setBoards(stay);
        for (Player p : players) {
            assertEquals("@",p.toString());
            killPlayer(p);
            assertEquals("X",p.toString());
        }
    }

    @Test
    void addExperience() {
        setBoards(stay);
        for (Player p : players) {
            assertEquals(0,p.getExperience());
            p.addExperience(100);
            assertEquals(50,p.getExperience());
        }
    }

    @Test
    void levelUp() {
        setBoards(stay);
        for (Player p : players) {
            assertEquals(1,p.getLevel());
            p.addExperience(100);
            assertEquals(2,p.getLevel());
            p.levelUp();
            assertEquals(3,p.getLevel());
        }
    }

    @Test
    void visit() {
        setBoards(stay);
        addEnemyToBoards(new Position(2,3));
        for (Player p : players) {
            while (!playerBoards.get(p).getEnemies().isEmpty())
                p.visit(playerBoards.get(p).getEnemies().getFirst());
            assertEquals(2,p.getLevel());
        }
        //other tests of visit (like walls and empty are at the takeTurn tests
    }

    @Test
    void postCombat() {
        setBoards(stay);
        addEnemyToBoards(new Position(2,3));
        for (Player p : players) {
            while (!playerBoards.get(p).getEnemies().isEmpty())
                p.visit(playerBoards.get(p).getEnemies().getFirst());
            assertEquals(true , playerBoards.get(p).getEnemies().isEmpty());
        }
    }

    @Test
    void takeTurn() {
        setBoards(right);
        for (Player p : players) {
            Position expected = p.getPosition().right();
            p.takeTurn();
            assertEquals(expected,p.getPosition());
        }
        setBoards(left);
        for (Player p : players) {
            Position expected = p.getPosition().left();
            p.takeTurn();
            assertEquals(expected,p.getPosition());
        }
        setBoards(up);
        for (Player p : players) {
            Position expected = p.getPosition().up();
            p.takeTurn();
            assertEquals(expected,p.getPosition());
        }
        setBoards(down);
        for (Player p : players) {
            Position expected = p.getPosition().down();
            p.takeTurn();
            assertEquals(expected,p.getPosition());
        }
    }

}