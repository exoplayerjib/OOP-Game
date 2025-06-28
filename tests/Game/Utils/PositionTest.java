package Game.Utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    // ───────────────────── constructors ─────────────────────

    @Test
    void defaultConstructorCreatesOrigin() {
        Position p = new Position();
        assertEquals(0, p.getX());
        assertEquals(0, p.getY());
        assertEquals("(0,0)", p.toString());
    }

    @Test
    void parameterizedConstructorStoresValues() {
        Position p = new Position(5, -3);
        assertEquals(5, p.getX());
        assertEquals(-3, p.getY());
        assertEquals("(5,-3)", p.toString());
    }

    // ───────────────────── movement helpers ─────────────────────

    @Test
    void upReturnsNewPositionWithYMinusOne() {
        Position p = new Position(2, 2);
        Position up = p.up();
        assertEquals(new Position(2, 1), up);
        assertNotSame(p, up);
        assertEquals(2, p.getY());          // original unchanged
    }

    @Test
    void downReturnsNewPositionWithYPlusOne() {
        Position p = new Position(2, 2);
        Position down = p.down();
        assertEquals(new Position(2, 3), down);
        assertNotSame(p, down);
        assertEquals(2, p.getY());
    }

    @Test
    void leftReturnsNewPositionWithXMinusOne() {
        Position p = new Position(2, 2);
        Position left = p.left();
        assertEquals(new Position(1, 2), left);
        assertNotSame(p, left);
        assertEquals(2, p.getX());
    }

    @Test
    void rightReturnsNewPositionWithXPlusOne() {
        Position p = new Position(2, 2);
        Position right = p.right();
        assertEquals(new Position(3, 2), right);
        assertNotSame(p, right);
        assertEquals(2, p.getX());
    }

    // ───────────────────── equals / toString ─────────────────────

    @Test
    void equalsConsidersCoordinates() {
        assertEquals(new Position(1, 1), new Position(1, 1));
        assertNotEquals(new Position(1, 1), new Position(1, 2));
        assertNotEquals(new Position(1, 1), null);
        assertNotEquals(new Position(1, 1), "not a position");
    }

    @Test
    void toStringFormat() {
        assertEquals("(7,4)", new Position(7, 4).toString());
    }
}
