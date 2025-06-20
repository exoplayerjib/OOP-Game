package Game.Tiles.Units.Actions;

import Game.Tiles.Units.Unit;

import java.util.function.Supplier;

public abstract class SpecialAbility implements Action{
    protected int range;
    protected Supplier<Unit> targets;
    public SpecialAbility(int range, Supplier<Unit> targets){
        this.range = range;
        this.targets = targets;
    }

    public void execute(){
        onCast();
    }

    protected abstract void onCast();

    public int getRange(){
        return range;
    }
}

