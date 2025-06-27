package Game.Tiles;

import Game.Callbacks.MessageCallback;
import Game.Tiles.Units.Unit;
import Game.Utils.Position;

public abstract class Tile {

    protected char tile;
    protected Position position;

    public Tile(char sym){
        this.tile = sym;
    }

    public Tile init(Position position){
        this.position = position;
        return this;
    }

    public char getTile() {
        return tile;
    }

    public double range(Tile other){
        return Math.sqrt(Math.pow(this.position.getX() - other.position.getX(), 2) + Math.pow(this.position.getY() - other.position.getY(),2));
    }

    public Position getPosition(){
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public abstract void accept(Unit visitor);

    @Override
    public String toString(){
        return ""+getTile();
    }

    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Tile tile = (Tile) other;
        return this.tile == tile.tile && position.equals(tile.position);
    }

}
