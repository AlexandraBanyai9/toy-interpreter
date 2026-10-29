package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompStmtTest {
    private PrgState state;
    private final IStmt first = new PrintStmt(new ValueExp(new IntValue(1)));
    private final IStmt second = new PrintStmt(new ValueExp(new IntValue(2)));

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), new NopStmt());
    }

    @Test
    void firstStatementIsOnTopOfStack() throws MyException {
        new CompStmt(first, second).execute(state);
        assertEquals(first, state.getExeStack().pop());
    }

    @Test
    void secondStatementIsRightBelowFirst() throws MyException {
        new CompStmt(first, second).execute(state);
        state.getExeStack().pop();
        assertEquals(second, state.getExeStack().pop());
    }

    @Test
    void doesNotExecuteAnythingItself() throws MyException {
        new CompStmt(first, second).execute(state);
        assertEquals(0, state.getOut().size());
    }
}