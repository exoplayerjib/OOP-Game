package Game.Tiles.Units;

import Game.Tiles.Tile;
import Game.Utils.Resource;

public abstract class Unit extends Tile {

    protected String name;
    protected Resource health;
    protected int attack;
    protected int defense;

    public Unit(char sym, String name, int healthCap, int attack, int defense) {
        super(sym);
        this.name = name;
        this.health = new Resource(healthCap, healthCap);
        this.attack = attack;
        this.defense = defense;
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
