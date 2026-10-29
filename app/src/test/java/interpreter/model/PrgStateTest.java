package interpreter.model;

import interpreter.exceptions.ADTException;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrgStateTest {
    @Test
    void constructorPushesProgramOnStack() throws ADTException {
        IStmt prg = new NopStmt();
        PrgState state = new PrgState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), prg);
        assertEquals(prg, state.getExeStack().pop());
    }

}