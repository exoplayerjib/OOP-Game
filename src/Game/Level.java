package Game;

import Game.Board.Board;
import Game.Board.GameBoard;
import Game.Callbacks.MessageCallback;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Players.Player;
import View.Input.InputQuery;
import View.Parser.TileFactory;

import java.io.IOException;
import java.nio.file.Path;

public class Level {
    Path pathToFile;
    private GameBoard board;
    private final Player player;
    private final MessageCallback messageCallback;
    TileFactory tileFactory = new TileFactory();
    InputQuery inputQuery;


    public Level(Path pathToFile, Player player, TileFactory tileFactory, MessageCallback messageCallback, InputQuery inputQuery) {
        this.player = player;
        this.pathToFile = pathToFile;
        this.tileFactory = tileFactory;
        this.inputQuery = inputQuery;
        this.messageCallback = messageCallback;

    }

    public Level init() throws IOException {
        board = new Board(pathToFile, player, tileFactory, messageCallback,inputQuery, this::onPlayerDeath);
        return this;
    }

    public void run(){
        while (!isLevelFinished())
            gameTick();
    }

    private void gameTick(){
        board.render();
        takeTurns();
        onTick();
    }

    private void takeTurns(){
        player.takeTurn();
        for (Enemy enemy : board.getEnemies())
            enemy.takeTurn();
    }

    private void onTick(){
        player.onTick();
        for(Enemy enemy : board.getEnemies())
            enemy.onTick();
    }

    private boolean isLevelFinished(){
        return board.getEnemies().isEmpty();
    }

    private void onPlayerDeath(){
        board.render();
        messageCallback.send("Player Died, Game is over!");
        System.exit(0);
    }

    public Path getPathToFile() {
        return pathToFile;
    }

}
