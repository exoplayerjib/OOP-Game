package Game.Tiles.Units.Actions;

import Game.Board.Board;
import Game.Board.GameBoard;
import Game.Tiles.Tile;
import Game.Tiles.Units.Unit;
import Game.Utils.Position;

public abstract class Movement implements Action {
    Unit unit;
    GameBoard board;
    public Movement(Unit unit, GameBoard board){
        this.unit = unit;
        this.board = board;
    }

    public static class Up extends Movement{
        public Up(Unit unit, GameBoard board){
            super(unit,board);
        }

        @Override
        public void execute() {
            Position newPos = unit.getPosition().up();
            Tile dest = board.getTile(newPos);
            dest.accept(unit);
        }
    }

    public static class Down extends Movement{
        public Down(Unit unit, GameBoard board){
            super(unit,board);
        }
        @Override
        public void execute() {
            Position newPos = unit.getPosition().down();
            Tile dest = board.getTile(newPos);
            dest.accept(unit);
        }
    }

    public static class Left extends Movement{
        public Left(Unit unit, GameBoard board){
            super(unit,board);
        }
        @Override
        public void execute() {
            Position newPos = unit.getPosition().left();
            Tile dest = board.getTile(newPos);
            dest.accept(unit);
        }
    }

    public static class Right extends Movement{
        public Right(Unit unit, GameBoard board){
            super(unit,board);
        }
        @Override
        public void execute() {
            Position newPos = unit.getPosition().right();
            Tile dest = board.getTile(newPos);
            dest.accept(unit);
        }
    }
    
    public static class Stay extends Movement{
        public Stay(Unit unit, GameBoard board) {
            super(unit, board);
        }

        @Override
        public void execute() {
            return;
        }
    }

}
