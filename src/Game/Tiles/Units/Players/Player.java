package Game.Tiles.Units.Players;

import Game.Tiles.Units.Unit;

public abstract class Player extends Unit {
    public static final char playerSymbol = '@';
    public static final int POINTS_MULTIPLIER = 50;
    protected int experience;
    protected int level;

    public Player(String name, int healthCap, int attack, int defense) {
        super(playerSymbol,name,healthCap,attack,defense);
        this.experience = 0;
        this.level = 1;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void addExperience(int experience) {
        this.experience += experience;
    }

    public int getLevel() {
        return level;
    }

    public void levelUp() {
        if ( experience - level * POINTS_MULTIPLIER < 0)
            throw new IllegalArgumentException("Not enough experience");
        experience = experience - level * POINTS_MULTIPLIER;
        level++;
        health.addCapacity(10 * level);
        health.restore();
        attack += 4 * level;
        defense += level;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        if (!super.equals(other)) return false;
        Player player = (Player) other;
        return experience == player.experience && level == player.level;
    }

}
