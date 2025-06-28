package Game.Tiles.Units.Players;

import Game.Tiles.Units.Enemies.Enemy;

public class Hunter extends Player {
    private static final int ARROW_INC = 10;

    private final int range;
    private int arrowCount;
    private int tickCount = 0;

    public Hunter(String name, int healthCap, int attack, int defense, int range){
        super(name, healthCap, attack, defense);
        this.range = range;
        arrowCount = ARROW_INC;
        specialAbility = new Shoot(range);
    }

    @Override
    protected int gainAttackAmount(){
        return super.gainAttackAmount() + 2 * getLevel();
    }

    @Override
    protected int gainDefenseAmount(){
        return super.gainDefenseAmount() + 1 * getLevel();
    }

    protected int gainArrowsAmount(){
        return level * ARROW_INC;
    }

    @Override
    public void levelUp() {
        super.levelUp();
        int arrows = gainArrowsAmount();
        arrowCount += arrows;
        messageCallback.send(String.format("\t\t+%d arrows",arrows));
    }
    public int getArrowCount() {
        return arrowCount;
    }

    public int getRange() {
        return range;
    }

    protected void reduceArrowCount(){
        if (arrowCount > 0)
            arrowCount--;
    }

    @Override
    protected boolean canCastAbility() {
        return arrowCount > 0 && board.getEnemies().stream().anyMatch(e -> range(e) <= getRange());
    }

    @Override
    protected void failedToCastMessage() {
        messageCallback.send(String.format("%s tried to cast %s, but there is either no arrows or no enemies in range.",
                getName(),specialAbility.getAbilityName()));
    }

    @Override
    protected void onTickActions(){
            if (tickCount == 10) {
                arrowCount += level;
                tickCount = 0;
        }
            tickCount++;
    }

    @Override
    public String description(){
        return String.format("%s\tArrows: %d\t Range: %d",super.description(),getArrowCount(),getRange());
    }

    private class Shoot extends SpecialAbility{

        private Shoot(int range){
            super(range,"Shoot");
        }

        @Override
        protected void onCast() {
            reduceArrowCount();
            this.targets = board.getEnemies().stream()
                    .filter(e -> range(e) <= getAbilityRange())
                    .sorted((e1, e2) -> Double.compare(range(e1), range(e2)))
                    .collect(java.util.stream.Collectors.toList());
            Enemy target = this.targets.getFirst();
            messageCallback.send(String.format("%s casts %s on %s",getName(),getAbilityName(),target.getName()));
            int defenderRoll = target.rollDefense();
            int damage = Math.max(0,getAttack()-defenderRoll);
            target.takeDamage(damage);
            messageCallback.send(String.format(" ~ %s hit %s for %d ability damage\n",getName(), target.getName(), damage));
            postCombat(target);
        }
    }

}
