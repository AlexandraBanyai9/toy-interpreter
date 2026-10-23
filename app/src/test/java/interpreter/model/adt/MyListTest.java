package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest {
    private IList<Integer> list;

    @BeforeEach
    void setUp(){
        list = new MyList<>();
    }

    @Test
    void getReturnsAddedElements() throws ADTException {
        list.add(1);
        list.add(2);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test
    void getWithNegativeIndexThrows() {
        var ex1 = assertThrows(ADTException.class, () -> list.get(-1));
        assertEquals("Index out of bounds", ex1.getMessage());
    }

    @Test
    void getWithIndexTooLargeThrows(){
        var ex2 = assertThrows(ADTException.class, () -> list.get(1));
        assertEquals("Index out of bounds", ex2.getMessage());
    }

    @Test
    void sizeGrowsWithAdd() {
        assertEquals(0, list.size());
        list.add(1);
        assertEquals(1, list.size());
    }
}