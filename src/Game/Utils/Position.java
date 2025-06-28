package Game.Utils;

public class Position {
    private int x;
    private int y;

    public Position() {
        this.x = 0;
        this.y = 0;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position up(){
        return new Position(x,y-1);
    }

    public Position down(){
        return new Position(x,y+1);
    }

    public Position left(){ return new Position(x-1,y); }

    public Position right(){ return new Position(x+1,y); }

    public int getX() {return x;}

    public int getY() {return y;}

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Position position = (Position) other;
        return x == position.x && y == position.y;
    }

    @Override
    public String toString() {
        return  "(" + x + "," + y + ")";
    }
}
