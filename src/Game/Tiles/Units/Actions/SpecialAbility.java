package Game.Tiles.Units.Actions;

import Game.Tiles.Units.Enemies.Enemy;

import java.util.List;

public abstract class SpecialAbility implements Action{
    protected int range;
    protected String name;
    protected List<Enemy> targets;

    public SpecialAbility(int range, String name){
        this.range = range;
        this.name = name;
    }

    public void execute(){
        onCast();
    }

    public void setTargets(List<Enemy> targets){
        this.targets = targets;
    }

    public String getName(){
        return name;
    }

    protected abstract void onCast();

    public int getRange(){
        return range;
    }
}

