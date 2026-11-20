package interpreter.model.stmt;

import static org.junit.jupiter.api.Assertions.*;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.ValueExp;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WriteHeapStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("v", new RefType(new IntType())).execute(state); // Ref int v
    }

    @Test
    void changesTheHeapLocation() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        new WriteHeapStmt("v", new ValueExp(new IntValue(30))).execute(state);
        assertEquals(new IntValue(30), state.getHeap().get(1));
    }

    @Test
    void variableNotAReferenceThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state);
        var s = new WriteHeapStmt("x", new ValueExp(new IntValue(1)));
        assertThrows(StatementException.class, () -> s.execute(state));
    }

    @Test
    void addressNotInTheHeapThrows() {
        // v is declared but never allocated: it holds address 0
        var s = new WriteHeapStmt("v", new ValueExp(new IntValue(1)));
        assertThrows(StatementException.class, () -> s.execute(state));
    }

    @Test
    void valueOfTheWrongTypeThrows() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        var s = new WriteHeapStmt("v", new ValueExp(new BoolValue(true)));
        assertThrows(StatementException.class, () -> s.execute(state));
    }
}