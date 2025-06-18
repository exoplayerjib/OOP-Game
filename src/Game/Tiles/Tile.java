package Game.Tiles;

import Game.Utils.Position;

public abstract class Tile {

    protected char tile;
    protected Position position;
    protected boolean walkable;

    public Tile(char sym, int x, int y){
        this.tile = sym;
        position = new Position(x,y);
    }

    public double range(Tile other){
        return Math.sqrt(Math.pow(this.position.getX() - other.position.getX(), 2) + Math.pow(this.position.getY() - other.position.getY(),2));
    }

    @Override
    public String toString(){
        return ""+this.tile;
    }

    @Override
    public boolean equals(Object other){
        if(other instanceof Tile that){
            return that.position.equals(this.position) &&
                    that.tile == this.tile &&
                    that.walkable == this.walkable;
        }
        return false;
    }

}
