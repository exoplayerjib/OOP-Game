public abstract class Tile {

    protected char character;
    protected int x;
    protected int y;

    public double range(Tile other){
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y,2));
    }
}
