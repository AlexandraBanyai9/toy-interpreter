package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class NopStmtTest {
    @Test
    void leavesStateUnchanged() throws MyException {
        PrgState state = PrgState.of(new NopStmt());
        state.getSymTable().put("a", new IntValue(1));

        PrgState result = new NopStmt().execute(state);

        assertSame(state, result);
        assertEquals(new IntValue(1), state.getSymTable().lookup("a"));
        assertEquals(0, state.getOut().size());
    }
}