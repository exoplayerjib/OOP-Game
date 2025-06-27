package Game.Board;

import Game.Callbacks.MessageCallback;
import Game.Tiles.BoardParts.Empty;
import Game.Tiles.Tile;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Players.Player;
import Game.Utils.Position;
import View.InputQuery;
import View.Parser.TileFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public  class Board {
    private final int rows;
    private final int cols;
    private final Tile[][] board;
    private final List<Enemy> enemies;
    private Player player;
    private final MessageCallback messageCallback;
    private final InputQuery inputQuery;
    private final TileFactory tileFactory = new TileFactory();

    public Board(Path file, Player player, MessageCallback messageCallback, InputQuery inputQuery) throws IOException {
        List<String> lines = Files.readAllLines(file);
        rows = lines.size();
        cols = lines.get(0).length();
        board = new Tile[cols][rows];
        enemies = new ArrayList<>();
        this.player = player;
        this.inputQuery = inputQuery;
        this.messageCallback = messageCallback;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char sym = lines.get(i).charAt(j);
                Position position = new Position(i, j);
                board[i][j] = createTile(sym, position); ///TODO no checks might cause errors
            }
        }
    }

    private Tile createTile(char sym, Position position){

        if (sym == player.getTile()){
            return player.init(position,messageCallback,this, inputQuery);
        }

        if (tileFactory.getBoardPartSymbols().contains(sym)){
            return tileFactory.createBoardPart(sym,position);
        }
        if (tileFactory.getEnemySymbols().contains(sym)){
            return tileFactory.createEnemy(sym,position,messageCallback,this);
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
            return board[position.getX()][position.getY()];
        else
            return null;
    }

    public void setTile(Position position, Tile tile){
        if (inBounds(position))
            board[position.getX()][position.getY()] = tile;
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
}
