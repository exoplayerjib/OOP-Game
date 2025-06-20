package Game.Tiles.BoardParts;

import Game.Tiles.Tile;
import Game.Tiles.Units.Unit;

public class Wall extends Tile {

    protected static final char wallTile = '#';

    public Wall(){
        super(wallTile);
    }
    public void accept(Unit visitor){
        visitor.visit(this);
    }
}
