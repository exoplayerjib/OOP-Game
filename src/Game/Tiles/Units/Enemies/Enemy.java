package Game.Tiles.Units.Enemies;

import Game.Tiles.BoardParts.Empty;
import Game.Tiles.BoardParts.Wall;
import Game.Tiles.Units.Players.Player;
import Game.Tiles.Units.Unit;

public abstract class Enemy extends Unit {
    protected int experienceValue;

    public Enemy(char sym, String name, int healthCap, int attack,int defense, int experienceValue) {
        super(sym ,name ,healthCap ,attack ,defense);
        this.experienceValue = experienceValue;
    }

    @Override
    public void accept(Unit visitor){
        visitor.visit(this);
    }

    @Override
    public void visit(Enemy enemy){
        return;
    }
    @Override
    public void visit(Player player){
        return;
    }
    @Override
    public void visit(Wall wall){
        return;
    }
    @Override
    public void visit(Empty empty){
        return;
    }

}
