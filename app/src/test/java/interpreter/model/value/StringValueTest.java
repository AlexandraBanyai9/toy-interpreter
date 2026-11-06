package interpreter.model.value;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import interpreter.model.type.StringType;

class StringValueTest {
    @Test
    void defaultValueIsEmptyString() {
        assertEquals(new StringValue(""), new StringType().defaultValue());
    }

    @Test
    void equalStringValuesHaveSameHashCode() {
        var a = new StringValue("test.in");
        var b = new StringValue("test.in");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}