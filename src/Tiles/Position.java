package Tiles;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {return x;}

    public int y() {return y;}

    public void setX(int x) {this.x = x;}

    public void setY(int y) {this.y = y;}

    public boolean equals(Object other) {
        if (other instanceof Position)
            return (this.x == ((Position) other).x()) && (this.y == ((Position) other).y()) ? true : false;
        return false;
    }

    public String toString() {
        return  "My position is: (" + x + "," + y + ")";
    }
}
