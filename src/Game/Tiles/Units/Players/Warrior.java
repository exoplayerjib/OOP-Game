package Game.Tiles.Units.Players;

import Game.Tiles.Units.Actions.SpecialAbility;
import Game.Tiles.Units.Unit;

import java.util.function.Supplier;

public class Warrior extends Player {
    private final int abilityCooldown;
    private int remainingCooldown;

    public Warrior(String name, int healthCap, int attack, int defense,int abilityCooldown){
        super(name, healthCap, attack, defense);
        this.abilityCooldown = abilityCooldown;
        remainingCooldown = 0;
    }

    @Override
    protected int gainHealthAmount(){
        return super.gainHealthAmount() + 5 * level;
    }

    @Override
    protected int gainAttackAmount() {
        return super.gainAttackAmount() + 2 * level;
    }

    @Override
    protected int gainDefenseAmount() {
        return super.gainDefenseAmount() + 1 * level;
    }

    @Override
    public void levelUp() {
        super.levelUp();
        remainingCooldown = 0;
    }

    @Override
    public void onTick() {
        reduceCooldown();
    }

    private void resetCooldown(){
        remainingCooldown = abilityCooldown;
    }

    private void reduceCooldown(){
        if (remainingCooldown > 0)
            remainingCooldown--;
    }

    private void reduceCooldown(int amount){
        if (remainingCooldown > 0)
            remainingCooldown = Math.max(0,remainingCooldown - amount);
    }

    @Override
    protected boolean canCastAbility() {
        return remainingCooldown == 0;
    }

    @Override
    public void castSpecialAbility() {
        if (canCastAbility()) {
        /// TODO implement when board is ready
        }
        return;
    }

    @Override
    public String description(){
        return super.description() + "\tAbility Cooldown: " + remainingCooldown + "/" + abilityCooldown;
    }

    private class AvengersShield extends SpecialAbility{
        private static final int RANGE = 3;

        public AvengersShield(Supplier<Unit> targets){
            super(RANGE,targets);
        }

        @Override
        protected void onCast() {
            resetCooldown();
            health.addAmount(10 * defense);
            targets.get().takeDamage( (int) (0.1 * health.getAmount()));
        }
    }
}
