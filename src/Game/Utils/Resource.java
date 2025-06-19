package Game.Utils;

public class Resource {
    private int capacity;
    private int amount;

    public Resource(int capacity, int amount){
        this.capacity = capacity;
        this.amount = amount;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void addCapacity(int capacity) {
        this.capacity += capacity;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void reduceAmount(int amount) {
        this.amount = Math.max(this.amount - amount,0);
    }

    public void restore() {
        amount = capacity;
    }

    @Override
    public String toString() {
        return amount+"/"+capacity;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Resource resource = (Resource) other;
        return capacity == resource.capacity && amount == resource.amount;
    }
}
