package Game.Board;

import Game.Callbacks.MessageCallback;
import Game.Tiles.Tile;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Players.Player;
import Game.Utils.Position;

import java.util.List;


/// FIXME make non-abstract
public abstract class Board {
//    private final int rows;
//    private final int cols;
//    private final Tile[][] board;
//    private final List<Enemy> enemies;
//    private Player player;
//    private final MessageCallback messageCallback;
    //TODO ALL!!!!

    public Board(){}

    public abstract void swapPositions(Tile tile1, Tile tile2);

    public abstract List<Enemy> getEnemies();

    public abstract List<Enemy> getLivingEnemies();

    public abstract Player getPlayer();

    public abstract void setTile(Position position, Tile tile);



}
