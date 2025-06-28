package Game.Tiles.Units.Players;

import Game.Tiles.Units.Actions.SpecialAbility;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Unit;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Warrior extends Player {
    private final int abilityCooldown;
    private int remainingCooldown;

    public Warrior(String name, int healthCap, int attack, int defense,int abilityCooldown){
        super(name, healthCap, attack, defense);
        this.abilityCooldown = abilityCooldown;
        remainingCooldown = 0;
        this.specialAbility = new AvengersShield();
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
    protected void onTickActions(){
        reduceCooldown();
    }

    private void resetCooldown(){
        remainingCooldown = abilityCooldown;
    }

    private void reduceCooldown(){
        if (remainingCooldown > 0)
            remainingCooldown--;
    }

    @Override
    protected boolean canCastAbility() {
        return remainingCooldown == 0;
    }

    @Override
    public void castSpecialAbility() {
        if (canCastAbility()) {
            specialAbility.execute();
            hasCasted = true;
        }
        else {
            messageCallback.send(
                    String.format("%s tried to cast %s, but there is a cooldown: (%d/%d)",
                    getName(),specialAbility.getAbilityName(),remainingCooldown,abilityCooldown));
        }
    }

    @Override
    public String description(){
        return super.description() + "\tAbility Cooldown: " + remainingCooldown + "/" + abilityCooldown;
    }

    private class AvengersShield extends SpecialAbility{
        private static final int RANGE = 3;
        private static final String NAME = "Avenger's Shield";

        public AvengersShield(){
            super(RANGE,NAME);
        }

        @Override
        protected void onCast() {
            this.targets =  board.getEnemies().
                            stream().
                            filter(e->range(e) <= this.getRange())
                            .collect(Collectors.toList());
            resetCooldown();
            health.addAmount(10 * defense);
            messageCallback.send(String.format("%s casts %s, increasing health by %d",getName(), getAbilityName(),10*defense));
            if (!targets.isEmpty()) {
                Enemy target = targets.get(random.nextInt(targets.size()));
                int defenderRoll = target.rollDefense();
                int damage = Math.max(0, (int) (0.1 * getCurrentHP()) - defenderRoll);
                target.takeDamage(damage);
                messageCallback.send(String.format(" ~ %s hit %s for %d ability damage\n",getName(), target.getName(), damage));
                postCombat(target);
            }
        }
    }
}
