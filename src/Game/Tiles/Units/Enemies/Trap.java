package Game.Tiles.Units.Enemies;

import Game.Tiles.Units.Players.Player;

public class Trap extends Enemy {

    private final int visibilityTime;
    private final int invisibilityTime;
    private int tickCount;
    private boolean visible;

    public Trap(char sym, String name, int healthCap, int attack, int defense, int experienceValue, int visibilityTime, int invisibilityTime){
        super(sym, name, healthCap, attack, defense, experienceValue);
        this.visibilityTime = visibilityTime;
        this.invisibilityTime = invisibilityTime;
        this.tickCount = 0;
        this.visible = true;
    }

    @Override
    public void takeTurn() {
        Player player = board.getPlayer();
        if (range(player) < 2){
            engageCombat(player);
        }
    }

    @Override
    public void onTick(){
        visible = tickCount < visibilityTime;
        if (tickCount == visibilityTime + invisibilityTime){
            tickCount = 0;
        }
        else
            tickCount++;
    }

    public boolean isVisible(){
        return visible;
    }

    @Override
    public String toString(){
        return visible ? super.toString() : ".";
    }

    @Override
    public String description(){
        return String.format("%s\tVisible: %s\tTimer: %d/%d+%d",
                super.description(), visible, tickCount,
                visibilityTime, invisibilityTime);
    }

}
