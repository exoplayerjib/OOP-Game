package Game.Tiles.BoardParts;

import Game.Tiles.Tile;

public class Wall extends Tile {

    protected static final char wallTile = '#';

    public Wall(int x, int y){
        super(wallTile);
    }
}
