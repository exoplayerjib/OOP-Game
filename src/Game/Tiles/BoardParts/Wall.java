package Game.Tiles.BoardParts;

import Game.Tiles.Tile;

public class Wall extends Tile {

    public Wall(int x, int y){
        super('#',x,y);
        this.walkable = false;
    }
}
