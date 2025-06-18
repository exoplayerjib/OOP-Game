package Game.Tiles.Units;

import Game.Tiles.Tile;

public abstract class Unit extends Tile {

    protected String name;
    protected Object health;
    protected int attack;
    protected int defence;

    public Unit(String name, Object health, int attack, int defence, char sym ,int x, int y) {
        super(sym,x,y);
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.defence = defence;
    }
}
