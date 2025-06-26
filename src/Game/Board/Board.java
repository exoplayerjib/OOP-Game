package Game.Board;

import Game.Callbacks.MessageCallback;
import Game.Tiles.BoardParts.Empty;
import Game.Tiles.Tile;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Players.Player;
import Game.Utils.Position;
import java.util.ArrayList;
import java.util.List;


public  class Board {
    private final int rows;
    private final int cols;
    private final Tile[][] board;
    private final List<Enemy> enemies;
    private Player player;
    private final MessageCallback messageCallback;
    //TODO ALL!!!!

    public Board(){
        this.rows = 10;
        this.cols = 10;
        this.board = new Tile[rows][cols];
        this.enemies = new ArrayList<>();
        this.player = null;
        this.messageCallback = null;
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
}
