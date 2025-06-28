package Game.Board;

import Game.Tiles.Tile;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Players.Player;
import Game.Utils.Position;

import java.util.List;

public interface GameBoard {
    boolean inBounds(Position position);
    Tile getTile(Position position);
    void setTile(Position position, Tile tile);
    void swapPositions(Tile operator, Tile operatee);
    Tile removeEnemy(Enemy enemy);
    Player getPlayer();
    List<Enemy> getEnemies();
    void render();
}
