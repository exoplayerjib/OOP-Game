package View.Parser;


import Game.Board.Board;
import Game.Callbacks.MessageCallback;
import Game.Tiles.BoardParts.Empty;
import Game.Tiles.BoardParts.Wall;
import Game.Tiles.Tile;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Enemies.Monster;
import Game.Tiles.Units.Enemies.Trap;
import Game.Tiles.Units.Players.Mage;
import Game.Tiles.Units.Players.Player;
import Game.Tiles.Units.Players.Rogue;
import Game.Tiles.Units.Players.Warrior;
import Game.Utils.Position;
import View.InputQuery;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TileFactory {

    private Map<Character,Supplier<Tile>> boardPartsRepository;
    private Map<Character,Supplier<Enemy>> enemyRepository;
    private List<Supplier<Player>> playerRepository;

    public TileFactory(){
        boardPartsRepository = boardPartsRepository();
        enemyRepository = enemyRepository();
        playerRepository = playerRepository();
    }

    private List<Supplier<Player>> playerRepository() {
        return Arrays.asList(
                () -> new Warrior("Jon Snow",300,30,4,3),
                () -> new Warrior("The Hound",400,20,6,5),
                () -> new Mage("Melisandre",100,5,1,300,30,15,5,6),
                () -> new Mage("Thoros of Myr",250,25,4,150,20,20,3,4),
                () -> new Rogue("Arya Stark",150,40,2,20),
                () -> new Rogue("Bronn",250,35,3,50)
        );
    }

    private Map<Character,Supplier<Enemy>> enemyRepository(){
        List<Supplier<Enemy>> enemies = Arrays.asList(
                () -> new Monster('s',"Lannister Solider",80,8,3,25,3),
                () -> new Monster('k', "Lannister Knight", 200, 14, 8, 50, 4),
                () -> new Monster('q', "Queen’s Guard", 400, 20, 15, 100, 5),
                () -> new Monster('z', "Wright", 600, 30, 15, 100, 3),
                () -> new Monster('b', "Bear‑Wright", 1000, 75, 30, 250, 4),
                () -> new Monster('g', "Giant‑Wright", 1500, 100, 40, 500, 5),
                () -> new Monster('w', "White Walker", 2000, 150, 50, 1000, 6),
                () -> new Monster('M', "The Mountain", 1000, 60, 25, 500, 6),
                () -> new Monster('C', "Queen Cersei", 100, 10, 10, 1000, 1),
                () -> new Monster('K', "Night’s King", 5000, 300, 150, 5000, 8),

                () -> new Trap('B', "Bonus Trap", 1, 1, 1, 250, 1, 5),
                () -> new Trap('Q', "Queen’s Trap", 250, 50, 10, 100, 3, 7),
                () -> new Trap('D', "Death Trap", 500, 100, 20, 250, 1, 10)

        );
        return enemies.stream().collect(Collectors.toMap(c -> c.get().getTile(), Function.identity()));
    }

    private Map<Character,Supplier<Tile>> boardPartsRepository() {
        List<Supplier<Tile>> boardParts = Arrays.asList(
                Wall::new,
                Empty::new
        );
        return boardParts.stream().collect(Collectors.toMap(c -> c.get().getTile(), Function.identity()));
    }

    public Tile createBoardPart(char sym, Position position){
        if (boardPartsRepository.containsKey(sym)){
            return boardPartsRepository.get(sym).get().init(position);
        }
        else {
            System.out.println("Error while loading file, no such board part: " + sym + " at position: " + position.toString() + "!");
            System.exit(-1);
            return null;
        }
    }

    public Enemy createEnemy(char sym, Position position, MessageCallback messageCallback, Board board){
        if (enemyRepository.containsKey(sym)){
            return enemyRepository.get(sym).get().init(position,messageCallback,board);
        }
        else {
            System.out.println("Error while loading file, no such enemy: " + sym + " at position: " + position.toString() + "!");
            System.exit(-1);
            return null;
        }
    }
    
    public List<Character> getEnemySymbols(){
        return enemyRepository.keySet().stream().toList();
    }

    public List<Character> getBoardPartSymbols(){
        return boardPartsRepository.keySet().stream().toList();
    }

    public Player createPlayer(int index){
        return playerRepository.get(index).get();
    }

}
