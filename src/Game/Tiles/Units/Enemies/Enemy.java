package Game.Tiles.Units.Enemies;

import Game.Board.Board;
import Game.Callbacks.MessageCallback;
import Game.Callbacks.PlayerDeathCallback;
import Game.Tiles.BoardParts.Empty;
import Game.Tiles.BoardParts.Wall;
import Game.Tiles.Units.Players.Player;
import Game.Tiles.Units.Unit;
import Game.Utils.Position;
import View.InputQuery;

public abstract class Enemy extends Unit {
    protected int experienceValue;

    public Enemy(char sym, String name, int healthCap, int attack, int defense, int experienceValue) {
        super(sym ,name ,healthCap ,attack ,defense);
        this.experienceValue = experienceValue;
    }

    public Enemy init(Position position, MessageCallback messageCallback, Board board){
        super.init(position,messageCallback,board);
        return this;
    }

    public int getExperienceValue() {
        return experienceValue;
    }

    @Override
    public void accept(Unit visitor){
        visitor.visit(this);
    }

    @Override
    public void visit(Enemy enemy){
        messageCallback.send(String.format("%s has bumped into %s",getName(),enemy.getName()));
    }
    @Override
    public void visit(Player player){
        engageCombat(player);
        if (!player.isAlive()){
            player.getDeathCallback().onDeath();
        }
    }

    @Override
    public void visit(Wall wall){
        return;
    }
    @Override
    public void visit(Empty empty){
        board.swapPositions(this,empty);
    }

}
