package Game.Tiles.Units.Players;

import Game.Tiles.BoardParts.Empty;
import Game.Tiles.BoardParts.Wall;
import Game.Tiles.Units.Actions.SpecialAbility;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Unit;

public abstract class Player extends Unit {
    public static final char playerSymbol = '@';
    protected static final int XP_REQ = 50;
    protected static final int ATTACK_ADD = 4;
    protected static final int DEFENSE_ADD = 1;
    protected static final int HEALTH_ADD = 10;
    protected int experience;
    protected int level;
    /// FIXME Might not need that
    protected SpecialAbility specialAbility;

    public Player(String name, int healthCap, int attack, int defense) {
        super(playerSymbol,name,healthCap,attack,defense);
        this.experience = 0;
        this.level = 1;
    }

    protected int gainHealthAmount() {
        return HEALTH_ADD * level;
    }

    protected int gainAttackAmount() {
        return ATTACK_ADD * level;
    }

    protected int gainDefenseAmount() {
        return DEFENSE_ADD * level;
    }

    public int getExperience() {
        return experience;
    }

    public int getReqXP() {
        return level * XP_REQ;
    }

    public int getLevel() {
        return level;
    }

    public void addExperience(int experience) {
        this.experience += experience;
        messageCallback.send(String.format("%s gained %d experience points", getName(), experience));
        int levelReq = getReqXP();
        while (experience >= levelReq) {
            levelUp();
            experience -= levelReq;
            levelReq = getReqXP();
        }
    }

    public void levelUp() {
        level++;
        int gainedHP = gainHealthAmount();
        int gainedATK = gainAttackAmount();
        int gainedDEF = gainDefenseAmount();
        health.addCapacity(gainedHP);
        health.restore();
        attack += gainedATK;
        defense += gainedDEF;
        messageCallback.send(String.format("%s leveled up to level %d! +%d HP, +%d Attack, +%d Defense", getName(), getLevel(), gainedHP, gainedATK, gainedDEF));
    }

    @Override
    public void visit(Enemy enemy){
        engageCombat(enemy);
        if(!enemy.isAlive()) {
            addExperience(enemy.getExperienceValue());
            /// TODO needs to implement swap position and removing the enemy from the game
        }
    }
    @Override
    public void visit(Player player){
        return;
    }
    @Override
    public void visit(Wall wall){
        messageCallback.send(String.format("%s was hit a wall!",getName()));
    }
    @Override
    public void visit(Empty empty){
        /// TODO implement movement when ready
        return;
    }

    @Override
    public void accept(Unit visitor){
        visitor.visit(this);
    }

    public int getSpecialAbilityRange() {
        return specialAbility.getRange();
    }

    protected abstract boolean canCastAbility();

    /// TODO Add Board as an argument
    public abstract void castSpecialAbility();


    @Override
    public String toString() {
        return isAlive() ? super.toString() : "X";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        if (!super.equals(other)) return false;
        Player player = (Player) other;
        return experience == player.experience && level == player.level;
    }

    @Override
    public String description(){
        return String.format("%s\tLevel: %d\tExperience: %d/%d", super.description(), getLevel(), getExperience(), getReqXP());
    }

}
