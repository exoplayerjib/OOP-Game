package Game.Tiles.Units.Players;

import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Resource;

public class Rogue extends Player{
    private static final int INITIAL_ENERGY = 100;
    private static final int ENERGY_INC = 100;

    private final int cost;
    private final Resource energy;

    public Rogue(String name, int healthCap, int attack, int defense, int cost){
        super(name, healthCap, attack, defense);
        this.cost = cost;
        energy = new Resource(INITIAL_ENERGY,INITIAL_ENERGY);
        this.specialAbility = new FanOfKnives();
    }

    @Override
    protected int gainAttackAmount(){
        return super.gainAttackAmount() + 3 * level;
    }

    @Override
    public void levelUp() {
        super.levelUp();
        energy.addCapacity(ENERGY_INC);
    }

    @Override
    protected void onTickActions() {
        energy.addAmount(10);
    }

    @Override
    public boolean canCastAbility() {
        return energy.getAmount() >= cost;
    }

    @Override
    protected void failedToCastMessage() {
        messageCallback.send(
                String.format("%s tried to cast %s, but there is not enough energy: (%d/%d)",
                        getName(),specialAbility.getAbilityName(),energy.getAmount(),cost));

    }

    @Override
    public String description(){
        return super.description() + "\tEnergy: " + energy.getAmount() + "/" + energy.getCapacity();
    }

    private class FanOfKnives extends SpecialAbility {
        private static final int RANGE = 2;
        private static final String NAME = "Fan of Knives";

        public FanOfKnives(){
            super(RANGE,NAME);
        }
        @Override
        protected void onCast() {
            energy.reduceAmount(cost);
            messageCallback.send(String.format("%s casts %s, reducing energy by %d",getName(),getAbilityName(),cost));
            this.targets = board.getEnemies()
                    .stream()
                    .filter(e -> range(e) <= this.getRange())
                    .collect(java.util.stream.Collectors.toList());
            for (Enemy target : targets) {
                int defenderRoll = target.rollDefense();
                int damage = Math.max(0,getAttack()-defenderRoll);
                target.takeDamage(damage);
                messageCallback.send(String.format(" ~ %s hit %s for %d ability damage\n",getName(), target.getName(), damage));
                postCombat(target);
            }
        }


    }

}
