package Game.Tiles.Units.Enemies;

import Game.Tiles.Units.Actions.Action;
import Game.Tiles.Units.Actions.Movement;
import Game.Tiles.Units.Players.Player;

import java.util.EnumSet;

public class Monster extends Enemy {
    public enum Directions{ UP, DOWN, LEFT, RIGHT}
    private int visionRange;

    public Monster(char sym, String name, int healthCap, int attack, int defense, int experienceValue, int visionRange){
        super( sym, name, healthCap, attack, defense, experienceValue);
        this.visionRange = visionRange;
    }

    public int getVisionRange() {
        return visionRange;
    }

    @Override
    public void takeTurn() {
        Directions direction = decideDirection(board.getPlayer());
        tryMove(direction);
    }

    @Override
    public void onTick(){
        return;
    }

    private Directions decideDirection(Player player) {
        double dist = range(player);
        if (dist < getVisionRange()) {                           // chase the player
            int dx = getPosition().getX() - player.getPosition().getX();
            int dy = getPosition().getY() - player.getPosition().getY();
            if (Math.abs(dx) > Math.abs(dy))
                return dx > 0 ? Directions.LEFT : Directions.RIGHT;
            else
                return dy > 0 ? Directions.UP : Directions.DOWN;
        }

        Directions[] opts = EnumSet.of(Directions.UP, Directions.DOWN,
                Directions.LEFT, Directions.RIGHT).toArray(Directions[]::new);
        return opts[random.nextInt(opts.length)];
    }

    protected void tryMove(Directions direction){
        Action movement = switch (direction){
            case UP -> new Movement.Up(this,board);
            case DOWN -> new Movement.Down(this,board);
            case LEFT -> new Movement.Left(this,board);
            case RIGHT -> new Movement.Right(this,board);
        };
        movement.execute();
    }

    @Override
    public String description(){
        return super.description() + "\tVision Range: " + visionRange;
    }

}
