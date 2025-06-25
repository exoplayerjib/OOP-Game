package Game.Tiles.Units.Actions;

import Game.Board.Board;
import Game.Tiles.Tile;
import Game.Tiles.Units.Unit;
import Game.Utils.Position;

public abstract class Movement implements Action {
    Unit unit;
    Board board;
    public Movement(Unit unit, Board board){
        this.unit = unit;
        this.board = board;
    }

    public static class Up extends Movement{
        public Up(Unit unit, Board board){
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
        public Down(Unit unit, Board board){
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
        public Left(Unit unit, Board board){
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
        public Right(Unit unit, Board board){
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
        public Stay(Unit unit, Board board) {
            super(unit, board);
        }

        @Override
        public void execute() {
            return;
        }
    }

}
