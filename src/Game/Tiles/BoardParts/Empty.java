package Game.Tiles.BoardParts;

import Game.Tiles.Tile;
import Game.Tiles.Units.Unit;

public class Empty extends Tile {
    public static final char emptyTile = '.';

    public Empty() {
        super(emptyTile);
    }
    public void accept(Unit visitor){
        visitor.visit(this);
    }
}
