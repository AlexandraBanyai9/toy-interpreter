package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrintStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), new NopStmt());
    }

    @Test
    void appendsValueToOut() throws MyException {
        new PrintStmt(new ValueExp(new IntValue(5))).execute(state);
        assertEquals(1, state.getOut().size());
        assertEquals(new IntValue(5), state.getOut().get(0));
    }

    @Test
    void printsValueOfVariable() throws MyException {
        state.getSymTable().put("a", new IntValue(5));
        new PrintStmt(new VarExp("a")).execute(state);
        assertEquals(new IntValue(5), state.getOut().get(0));
    }

    @Test
    void keepsPrintingOrder() throws MyException {
        new PrintStmt(new ValueExp(new IntValue(1))).execute(state);
        new PrintStmt(new ValueExp(new IntValue(5))).execute(state);
        assertEquals(new IntValue(1), state.getOut().get(0));
        assertEquals(new IntValue(5), state.getOut().get(1));
    }

    @Test
    void undefinedVariableThrows() {
        IStmt s = new PrintStmt(new VarExp("x"));
        assertThrows(ExpressionException.class, () -> s.execute(state));
    }
}