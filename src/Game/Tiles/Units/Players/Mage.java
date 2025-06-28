package Game.Tiles.Units.Players;

import Game.Tiles.Units.Enemies.Enemy;
import Game.Utils.Resource;

import java.util.stream.Collectors;

public class Mage extends Player{
    private static final int MANA_INCREASE = 25;
    private static final int SPELL_POWER_INCREASE = 10;

    private final Resource mana;
    private final int abilityCost;
    private int spellPower;

    public Mage(String name, int healthCap, int attack, int defense, int initialMana,int abilityCost,
                int spellPower, int hitCount, int abilityRange){
        super(name, healthCap, attack, defense);
        mana = new Resource(initialMana,initialMana/4);
        this.abilityCost = abilityCost;
        this.spellPower = spellPower;
        this.specialAbility = new Blizzard(abilityRange,hitCount);
    }

    private int getManaInc(){return MANA_INCREASE * level;}
    private int getSpellPowerInc(){return SPELL_POWER_INCREASE * level;}

    @Override
    public void levelUp() {
        super.levelUp();
        int manaIncrease = getManaInc();
        mana.addCapacity( manaIncrease );
        mana.addAmount(mana.getCapacity()/4);
        int spellPowerIncrease = getSpellPowerInc();
        spellPower += spellPowerIncrease;
        messageCallback.send(String.format("\t\t+%d Mana, +%d Spell Power", manaIncrease,spellPowerIncrease));
    }

    @Override
    protected void onTickActions() {
        mana.addAmount(level);
    }

    @Override
    protected boolean canCastAbility() {
        return mana.getAmount() >= abilityCost;
    }

    @Override
    protected void failedToCastMessage() {
        messageCallback.send(String.format("%s tried to cast %s, but there is not enough mana: %d/%d",
                getName(),specialAbility.getAbilityName(),mana.getAmount(),abilityCost));

    }

    @Override
    public String description(){
        return super.description() + "\tMana: " + mana.getAmount() + "/" + mana.getCapacity() +
                "\tSpell Power: " + spellPower;
    }

    private class Blizzard extends SpecialAbility{
        private final int hitCount;

        public Blizzard(int range, int hitCount){
            super(range, "Blizzard");
            this.hitCount = hitCount;
        }

        @Override
        protected void onCast() {
            mana.reduceAmount(abilityCost);
            messageCallback.send(String.format("%s casts %s, reducing mana by %d",getName(),getAbilityName(),abilityCost));
            for (int hits = 0; hits < hitCount; hits++) {
                this.targets = board.getEnemies()
                        .stream()
                        .filter(e -> range(e) <= this.getRange())
                        .collect(Collectors.toList());
                if (targets.isEmpty())
                    return;
                else {
                    Enemy target = targets.get(random.nextInt(targets.size()));
                    int defenderRoll = target.rollDefense();
                    int damage = Math.max(0,spellPower - defenderRoll);
                    target.takeDamage(damage);
                    messageCallback.send(String.format(" ~ %s hit %s for %d ability damage\n",getName(), target.getName(), damage));
                    postCombat(target);
                }
            }
        }

    }
}

