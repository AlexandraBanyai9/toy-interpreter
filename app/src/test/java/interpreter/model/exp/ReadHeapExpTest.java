package interpreter.model.exp;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.IHeap;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyHeap;
import interpreter.model.type.IntType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReadHeapExpTest {
    private IDictionary<String, IValue> symTable;
    private IHeap heap;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
        heap = new MyHeap();
    }

    @Test
    void readsTheValueStoredAtTheAddress() throws MyException {
        int address = heap.allocate(new IntValue(20));
        symTable.put("v", new RefValue(address, new IntType()));
        IExp e = new ReadHeapExp(new VarExp("v"));
        assertEquals(new IntValue(20), e.eval(symTable, heap));
    }

    @Test
    void nonReferenceExpressionThrows() {
        IExp e = new ReadHeapExp(new ValueExp(new IntValue(1)));
        assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
    }

    @Test
    void addressNotInTheHeapThrows() {
        symTable.put("v", new RefValue(5, new IntType()));
        IExp e = new ReadHeapExp(new VarExp("v"));
        assertThrows(ExpressionException.class, () -> e.eval(symTable, heap));
    }
}