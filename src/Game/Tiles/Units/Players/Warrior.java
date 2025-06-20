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
    protected boolean canCastAbility() {
        return remainingCooldown == 0;
    }

    @Override
    public void levelUp() {
        super.levelUp();
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
    public void onTick() {
        if (remainingCooldown > 0) {
            remainingCooldown--;
        }
    }

    @Override
    public void castAbility() {
        if(canCastAbility()){
            specialAbility.execute();
        }
    }

    private class AvengersShield extends SpecialAbility{

        private static final int RANGE = 3;

        public AvengersShield(Supplier<Unit> targets){
            super(RANGE,targets);
        }

        @Override
        protected void onCast() {
            remainingCooldown = abilityCooldown;
            health.setAmount(health.getCapacity() + 10 * defense);
            /// implement Combat and make take 10% of this.health as damage
        }
    }

}
