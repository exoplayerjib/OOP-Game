package Game.Utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTest {

    private Resource resource;   // starts with capacity 100, amount 50

    @BeforeEach
    void setUp() {
        resource = new Resource(100, 50);
    }

    @Test
    void getCapacity() {
        assertEquals(100, resource.getCapacity());
    }

    @Test
    void setCapacityKeepsAmountUntouchedAndAllowsShrinkBelowAmount() {
        resource.setCapacity(150);
        assertEquals(150, resource.getCapacity());
        assertEquals(50, resource.getAmount(), "amount should stay unchanged");

        // should not change the capacity
        resource.setCapacity(40);
        assertEquals(150, resource.getCapacity());
        assertEquals(50, resource.getAmount());
    }

    @Test
    void addCapacityIncreasesCumulatively() {
        resource.addCapacity(30);
        assertEquals(130, resource.getCapacity());
        resource.addCapacity(20);
        assertEquals(150, resource.getCapacity());
    }

    // ─────────────────────────── amount ───────────────────────────

    @Test
    void getAmount() {
        assertEquals(50, resource.getAmount());
    }

    @Test
    void setAmountWithinCapacity() {
        resource.setAmount(80);
        assertEquals(80, resource.getAmount());
    }

    @Test
    void setAmountAboveCapacityClamps() {
        resource.setAmount(120);   // > 100
        assertEquals(100, resource.getAmount());
    }

    @Test
    void addAmountNormal() {
        resource.addAmount(20);    // 50 → 70
        assertEquals(70, resource.getAmount());
    }

    @Test
    void addAmountDoesNotExceedCapacity() {
        resource.addAmount(1000);  // way over
        assertEquals(100, resource.getAmount());
    }

    @Test
    void reduceAmountNormal() {
        resource.reduceAmount(30); // 50 → 20
        assertEquals(20, resource.getAmount());
    }

    @Test
    void reduceAmountFloorsAtZero() {
        resource.reduceAmount(1000); // 50 → 0
        assertEquals(0, resource.getAmount());
    }

    @Test
    void restoreRefillsToCapacity() {
        resource.reduceAmount(40);   // 50 → 10
        resource.restore();
        assertEquals(resource.getCapacity(), resource.getAmount());
    }


    @Test
    void toStringFormat() {
        assertEquals("50/100", resource.toString());
    }

    @Test
    void equalsComparesBothFields() {
        assertEquals(new Resource(100, 50), resource);
        assertNotEquals(new Resource(150, 50), resource);
        assertNotEquals(new Resource(100, 40), resource);
    }
}
