package Game;

import Game.Board.Board;
import Game.Callbacks.MessageCallback;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Players.Player;
import View.Input.InputQuery;
import View.Parser.TileFactory;

import java.io.IOException;
import java.nio.file.Path;

public class Level {
    private final Board board;
    private final Player player;
    private final MessageCallback messageCallback;


    public Level(Path pathToFile, Player player, TileFactory tileFactory, MessageCallback messageCallback, InputQuery inputQuery) throws IOException {
        this.player = player;
        this.messageCallback = messageCallback;
        board = new Board(pathToFile, player, tileFactory, messageCallback,inputQuery, this::onPlayerDeath);
    }

    public void run(){
        while (!isLevelFinished()){
            takeTurns();
            onTick();
        }
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

}
