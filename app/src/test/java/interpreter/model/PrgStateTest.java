package interpreter.model;

import interpreter.exceptions.ADTException;
import interpreter.model.stmt.IStmt;
import interpreter.model.stmt.NopStmt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

class PrgStateTest {
    @Test
    void constructorPushesProgramOnStack() throws ADTException {
        IStmt prg = new NopStmt();
        PrgState state = PrgState.of(prg);
        assertSame(prg, state.getExeStack().pop());
    }
}