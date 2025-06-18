package Game.Tiles.BoardParts;

import Game.Tiles.Tile;

public class Empty extends Tile {
    public Empty(int x, int y){
        super('.',x,y);
        this.walkable = true;
    }
}
