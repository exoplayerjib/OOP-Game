package Game.Tiles.Units;

import Game.Board.Board;
import Game.Callbacks.MessageCallback;
import Game.Tiles.BoardParts.*;
import Game.Tiles.Tile;
import Game.Tiles.Units.Actions.Movement;
import Game.Tiles.Units.Players.Player;
import Game.Utils.*;
import Game.Tiles.Units.Enemies.Enemy;

import java.util.HashMap;
import java.util.Random;
import java.util.function.Function;


public abstract class Unit extends Tile {

    protected String name;
    protected Resource health;
    protected int attack;
    protected int defense;
    protected MessageCallback messageCallback;
    protected Board board;
    protected Random random = new Random();

    /// FIXME might not need this
    protected HashMap<Position, Function<Unit,Movement>> movementMap = new HashMap<>(){{
        put(position.up(),unit -> new Movement.Up(unit,board));
        put(position.down(),unit -> new Movement.Down(unit,board));
        put(position.left(),unit -> new Movement.Left(unit,board));
        put(position.right(),unit -> new Movement.Right(unit,board));
        put(position,unit -> new Movement.Stay(unit,board));
    }};

    public Unit(char sym, String name, int healthCap, int attack, int defense) {
        super(sym);
        this.name = name;
        this.health = new Resource(healthCap, healthCap);
        this.attack = attack;
        this.defense = defense;
    }


    public Unit init(Position position, MessageCallback messageCallback, Board board) {
        super.init(position);
        this.messageCallback = messageCallback;
        this.board = board;
        return this;
    }

    public boolean isAlive() {
        return health.getAmount() > 0;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getCurrentHP() {
        return health.getAmount();
    }

    public int getMaxHP() {
        return health.getCapacity();
    }

    public String getName() {
        return name;
    }

    public void takeDamage(int damage){
        if (damage > 0)
            health.reduceAmount(damage);
    }

    protected void tryMove(Position position){
        if (movementMap.containsKey(position)) {
            Movement movement = movementMap.get(position).apply(this);
            movement.execute();
        }
    }

    abstract void takeTurn();

    abstract public void onTick();

    public abstract void visit(Empty empty);
    public abstract void visit(Wall wall);
    public abstract void visit(Player player);
    public abstract void visit(Enemy enemy);


    public int rollDefense(){
        int defenseRoll = random.nextInt(getDefense()+1);
        messageCallback.send(String.format("%s rolled %d defense points",getName(),defenseRoll));
        return defenseRoll;
    }

    public int rollAttack(){
        int attackRoll = random.nextInt(getAttack()+1);
        messageCallback.send(String.format("%s rolled %d attack points",getName(),attackRoll));
        return attackRoll;
    }

    protected void engageCombat(Unit defender){
        Random random = new Random();
        messageCallback.send(String.format("%s engaged in combat with %s",getName(),defender.getName()));
        messageCallback.send(description());
        messageCallback.send(defender.description());
        int attackerRoll = this.rollAttack();
        int defenderRoll = defender.rollDefense();
        int damage = Math.max(0,attackerRoll - defenderRoll);
        messageCallback.send(String.format("%s dealt %d damage to %s",getName(),damage,defender.getName()));
        defender.takeDamage(damage);
        if (!defender.isAlive()){
            messageCallback.send(String.format("%s killed %s",getName(),defender.getName()));
        }
    }

    public String description() {
        return String.format("Name: %s\tHealth: (%d/%d)\tAttack: %d\tDefense: %d",getName(),getCurrentHP(),getMaxHP(),getAttack(),getDefense());
    }

    @Override
    public boolean equals(Object other){
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Unit unit = (Unit) other;
        return attack == unit.attack &&
                defense == unit.defense &&
                name.equals(unit.name) &&
                health.equals(unit.health);
    }
}
