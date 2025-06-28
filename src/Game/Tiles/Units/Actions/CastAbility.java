package Game.Tiles.Units.Actions;

import Game.Tiles.Units.Players.Player;

public class CastAbility implements Action{
    Player player;

    public CastAbility(Player player){
        this.player = player;
    }

    @Override
    public void execute() {
        player.castSpecialAbility();
    }
}
