package Game.Tiles.Units;

import Game.Callbacks.MessageCallback;
import Game.Tiles.BoardParts.*;
import Game.Tiles.Tile;
import Game.Tiles.Units.Players.Player;
import Game.Utils.*;
import Game.Tiles.Units.Enemies.Enemy;


public abstract class Unit extends Tile {

    protected String name;
    protected Resource health;
    protected int attack;
    protected int defense;
    protected MessageCallback messageCallback;

    public Unit(char sym, String name, int healthCap, int attack, int defense) {
        super(sym);
        this.name = name;
        this.health = new Resource(healthCap, healthCap);
        this.attack = attack;
        this.defense = defense;
    }


    public Unit init(Position position, MessageCallback messageCallback) {
        super.init(position);
        this.messageCallback = messageCallback;
        return this;
    }

    public boolean isAlive() {
        return health.getAmount() > 0;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getCurrentHP() {
        return health.getAmount();
    }

    public int getMaxHP() {
        return health.getCapacity();
    }

    public String getName() {
        return name;
    }

    abstract public void onTick();

    public abstract void visit(Empty empty);
    public abstract void visit(Wall wall);
    public abstract void visit(Player player);
    public abstract void visit(Enemy enemy);

    public String description() {
        return String.format("Name: %s\tHealth: (%d/%d)\tAttack: %d\tDefense: %d",getName(),getCurrentHP(),getMaxHP(),getAttack(),getDefense());
    }

    @Override
    public boolean equals(Object other){
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Unit unit = (Unit) other;
        return attack == unit.attack &&
                defense == unit.defense &&
                name.equals(unit.name) &&
                health.equals(unit.health);
    }
}
