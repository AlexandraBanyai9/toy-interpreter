package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.type.BoolType;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VarDeclStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), new NopStmt());
    }

    @Test
    void declaresIntVariableWithDefaultZero() throws MyException {
        new VarDeclStmt("a", new IntType()).execute(state);
        assertEquals(new IntValue(0), state.getSymTable().lookup("a"));
    }

    @Test
    void declaresBoolVariableWithDefaultFalse() throws MyException {
        new VarDeclStmt("b", new BoolType()).execute(state);
        assertEquals(new BoolValue(false), state.getSymTable().lookup("b"));
    }

    @Test
    void declaringTwiceThrows() throws MyException {
        IStmt decl = new VarDeclStmt("a", new IntType());
        decl.execute(state);
        assertThrows(StatementException.class, () -> decl.execute(state));
    }
}