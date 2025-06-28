package Game.Board;

import Game.Callbacks.MessageCallback;
import Game.Callbacks.PlayerDeathCallback;
import Game.Tiles.BoardParts.Empty;
import Game.Tiles.Tile;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Players.Player;
import Game.Utils.Position;
import View.Input.InputQuery;
import View.Parser.TileFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public  class Board implements GameBoard {
    private final int rows;
    private final int cols;
    private final Tile[][] board;
    private final List<Enemy> enemies;
    private Player player;
    private final MessageCallback messageCallback;
    PlayerDeathCallback playerDeathCallback;
    private final InputQuery inputQuery;
    private final TileFactory tileFactory;

    public Board(Path file, Player player, TileFactory tileFactory
                 ,MessageCallback messageCallback, InputQuery inputQuery, PlayerDeathCallback playerDeathCallback) throws IOException {
        List<String> lines = Files.readAllLines(file);
        rows = lines.size();
        cols = lines.getFirst().length();
        board = new Tile[rows][cols];
        enemies = new ArrayList<>();
        this.player = player;
        this.tileFactory = tileFactory;
        this.inputQuery = inputQuery;
        this.messageCallback = messageCallback;
        this.playerDeathCallback = playerDeathCallback;


        for (int y = 0; y < rows; y++) {
            String line = lines.get(y);
            for (int x = 0; x < cols; x++) {
                char sym = line.charAt(x);
                Position position = new Position(x, y);
                board[y][x] = createTile(sym, position);
            }
        }
    }

    private Tile createTile(char sym, Position position){

        if (sym == player.getTile()){
            return player.init(position,messageCallback,this, inputQuery, playerDeathCallback);
        }

        if (tileFactory.getBoardPartSymbols().contains(sym)){
            return tileFactory.createBoardPart(sym,position);
        }
        if (tileFactory.getEnemySymbols().contains(sym)){
            Enemy enemy = tileFactory.createEnemy(sym,position,messageCallback,this);
            enemies.add(enemy);
            return enemy;
        }
        else {
            System.out.println("Error while loading file, no such tile: " + sym + " at position: " + position.toString() + "!");
            System.exit(-1);
            return null;
        }
    }

    public boolean inBounds(Position position){
        return position.getX() >= 0 && position.getX() < cols && position.getY() >= 0 && position.getY() < rows;
    }

    public Tile getTile(Position position){
        if (inBounds(position))
            return board[position.getY()][position.getX()];
        else
            return null;
    }

    public void setTile(Position position, Tile tile){
        if (inBounds(position))
            board[position.getY()][position.getX()] = tile;
    }

    public void swapPositions(Tile operator, Tile operatee){
        Position p1 = operator.getPosition();
        Position p2 = operatee.getPosition();
        operator.setPosition(p2);
        operatee.setPosition(p1);
        setTile(p2,operator);
        setTile(p1,operatee);
    }

    public Tile removeEnemy(Enemy enemy){
        enemies.remove(enemy);
        Empty empty = new Empty();
        empty.init(enemy.getPosition());
        setTile(enemy.getPosition(), empty);
        return empty;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Enemy> getEnemies(){
        return enemies;
    }

    public void render(){
        StringBuilder line = new StringBuilder(cols);
        for (int row = 0; row < rows; row++) {
            line.setLength(0);
            for (int col = 0; col < cols; col++) {
                line.append(board[row][col].toString());
            }
            messageCallback.send(line.toString());
        }
        messageCallback.send(player.description());
    }


    //TODO delete only for debugging
    @Override
    public String toString() {
        StringBuilder board = new StringBuilder(rows * cols);
        StringBuilder line = new StringBuilder(cols);
        for (int row = 0; row < rows; row++) {
            line.setLength(0);
            for (int col = 0; col < cols; col++) {
                line.append(this.board[row][col].toString());
            }
            board.append(line).append('\n');
        }
        return board.toString();
    }
}
