package interpreter.model.stmt;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.exp.ValueExp;
import interpreter.model.exp.VarExp;
import interpreter.model.type.IntType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssignStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() {
        state = new PrgState(new MyStack<>(), new MyDictionary<>(),
                new MyList<>(), new NopStmt());
    }

    @Test
    void assignsValueToDeclaredVariable() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        new AssignStmt("v", new ValueExp(new IntValue(2))).execute(state);
        assertEquals(new IntValue(2), state.getSymTable().lookup("v"));
    }

    @Test
    void assignsValueOfAnotherVariable() throws MyException {
        new VarDeclStmt("a", new IntType()).execute(state);
        new VarDeclStmt("b", new IntType()).execute(state);
        new AssignStmt("a", new ValueExp(new IntValue(7))).execute(state);
        new AssignStmt("b", new VarExp("a")).execute(state);
        assertEquals(new IntValue(7), state.getSymTable().lookup("b"));
    }

    @Test
    void assigningToUndeclaredVariableThrows() {
        IStmt s = new AssignStmt("v", new ValueExp(new IntValue(2)));
        var ex = assertThrows(StatementException.class, () -> s.execute(state));
        assertEquals("variable v was not declared", ex.getMessage());
    }

    @Test
    void typeMismatchThrows() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        IStmt s = new AssignStmt("v", new ValueExp(new BoolValue(true)));
        assertThrows(StatementException.class, () -> s.execute(state));
    }

    @Test
    void failedAssignmentLeavesVariableUnchanged() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        try {
            new AssignStmt("v", new ValueExp(new BoolValue(true))).execute(state);
        } catch (StatementException ignored) { }
        assertEquals(new IntValue(0), state.getSymTable().lookup("v"));
    }

    @Test
    void expressionErrorIsPropagated() throws MyException {
        new VarDeclStmt("v", new IntType()).execute(state);
        IStmt s = new AssignStmt("v", new VarExp("missing"));
        assertThrows(ExpressionException.class, () -> s.execute(state));
    }
}