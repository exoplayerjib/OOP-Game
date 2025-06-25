package Game.Tiles.Units.Players;

import Game.Board.Board;
import Game.Callbacks.MessageCallback;
import Game.Tiles.BoardParts.Empty;
import Game.Tiles.BoardParts.Wall;
import Game.Tiles.Tile;
import Game.Tiles.Units.Actions.Action;
import Game.Tiles.Units.Actions.Movement;
import Game.Tiles.Units.Actions.SpecialAbility;
import Game.Tiles.Units.Enemies.Enemy;
import Game.Tiles.Units.Unit;
import Game.Utils.Position;
import View.InputQuery;

import java.util.Map;
import java.util.function.Function;

public abstract class Player extends Unit {
    public static final char playerSymbol = '@';
    protected static final int XP_REQ = 50;
    protected static final int ATTACK_ADD = 4;
    protected static final int DEFENSE_ADD = 1;
    protected static final int HEALTH_ADD = 10;
    protected int experience;
    protected int level;
    protected SpecialAbility specialAbility;
    protected InputQuery inputQuery;

    private static final Map<Character, Function<Player, Action>> ACTIONS = Map.of(
            'w', p -> new Movement.Up(p, p.board),
            's', p -> new Movement.Down(p, p.board),
            'a', p -> new Movement.Left(p, p.board),
            'd', p -> new Movement.Right(p, p.board),
            'q', p -> new Movement.Stay(p, p.board),          // “do nothing”
            'e', Player::createAbilityAction                 // see helper below
    );

    public Player(String name, int healthCap, int attack, int defense) {
        super(playerSymbol,name,healthCap,attack,defense);
        this.experience = 0;
        this.level = 1;
    }

    public Player init(Position position, MessageCallback messageCallback, Board board, InputQuery inputQuery){
        super.init(position,messageCallback,board);
        this.inputQuery = inputQuery;
        return this;
    }

    protected int gainHealthAmount() {
        return HEALTH_ADD * level;
    }

    protected int gainAttackAmount() {
        return ATTACK_ADD * level;
    }

    protected int gainDefenseAmount() {
        return DEFENSE_ADD * level;
    }

    public int getExperience() {
        return experience;
    }

    public int getReqXP() {
        return level * XP_REQ;
    }

    public int getLevel() {
        return level;
    }

    public void addExperience(int experience) {
        this.experience += experience;
        messageCallback.send(String.format("%s gained %d experience points", getName(), experience));
        int levelReq = getReqXP();
        while (experience >= levelReq) {
            levelUp();
            experience -= levelReq;
            levelReq = getReqXP();
        }
    }

    public void levelUp() {
        level++;
        int gainedHP = gainHealthAmount();
        int gainedATK = gainAttackAmount();
        int gainedDEF = gainDefenseAmount();
        health.addCapacity(gainedHP);
        health.restore();
        attack += gainedATK;
        defense += gainedDEF;
        messageCallback.send(String.format("%s leveled up to level %d! +%d HP, +%d Attack, +%d Defense", getName(), getLevel(), gainedHP, gainedATK, gainedDEF));
    }

    @Override
    public void visit(Enemy enemy){
        engageCombat(enemy);
        Tile empty = postCombat(enemy);
        if (empty != null)
            board.swapPositions(this, empty);
    }

    protected Tile postCombat(Enemy enemy){
        if(!enemy.isAlive()) {
            int xpValue = enemy.getExperienceValue();
            messageCallback.send(String.format("%s killed %s, gaining %d experience points!",getName(),enemy.getName(),xpValue));
            addExperience(xpValue);
            return board.removeEnemy(enemy); ///might cause errors check
        }
        return null;
    }

    @Override
    public void visit(Player player){
        return;
    }
    @Override
    public void visit(Wall wall){
        messageCallback.send(String.format("%s hit a wall!",getName()));
    }
    @Override
    public void visit(Empty empty){
        board.swapPositions(this,empty);
    }

    @Override
    public void accept(Unit visitor){
        visitor.visit(this);
    }


    protected abstract boolean canCastAbility();

    public abstract void castSpecialAbility();

    private Action createAbilityAction() {
        return this::castSpecialAbility;
    }

    //TODO might not need this
    protected Player getPlayer(){ //used in special abilities
        return this;
    }
    public void takeTurn() {
        char input = inputQuery.getInput();
        if (ACTIONS.containsKey(input))
            ACTIONS.get(input).apply(this).execute();
    }

    @Override
    public String toString() {
        return isAlive() ? super.toString() : "X";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        if (!super.equals(other)) return false;
        Player player = (Player) other;
        return experience == player.experience && level == player.level;
    }

    @Override
    public String description(){
        return String.format("%s\tLevel: %d\tExperience: %d/%d", super.description(), getLevel(), getExperience(), getReqXP());
    }
}
