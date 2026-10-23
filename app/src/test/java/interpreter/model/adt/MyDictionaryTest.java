package interpreter.model.adt;

import interpreter.exceptions.ADTException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyDictionaryTest {
    private IDictionary<String,Integer> dict;

    @BeforeEach
    void setUp(){ dict = new MyDictionary<>(); }

    @Test
    void isDefinedTest() {
        dict.put("a", 1);
        assertTrue(dict.isDefined("a"));
        assertFalse(dict.isDefined("b"));
    }

    @Test
    void lookupOnInvalidKeyThrows(){
        var ex = assertThrows(ADTException.class, () -> dict.lookup("a"));
        assertEquals("Key not found", ex.getMessage());
    }

    @Test
    void lookUpOnValidKeyReturnsValue() throws ADTException{
        dict.put("a", 1);
        assertEquals(1, dict.lookup("a"));
    }

    @Test
    void putAddsValueAtGivenKey()  throws ADTException{
        dict.put("a", 1);
        assertTrue(dict.isDefined("a"));
        assertEquals(1, dict.lookup("a"));
    }

    @Test
    void updateOnInvalidKeyThrows(){
        var ex = assertThrows(ADTException.class, () -> dict.update("a", 1));
        assertEquals("Key not found", ex.getMessage());
    }

    @Test
    void updateOnValidKeyChangesValue() throws ADTException{
        dict.put("a", 1);
        dict.update("a", 2);
        assertEquals(2, dict.lookup("a"));
    }
}