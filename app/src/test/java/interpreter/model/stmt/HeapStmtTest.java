package interpreter.model.stmt;


import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.*;
import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HeapStmtTest {
    private PrgState state;

    @BeforeEach
    void setUp() throws MyException {
        state = PrgState.of(new NopStmt());
        new VarDeclStmt("v", new RefType(new IntType())).execute(state); // Ref int v
    }

    @Test
    void newAllocatesAtAddressOneAndUpdatesTheVariable() throws MyException {
        new NewStmt("v", new ValueExp(new IntValue(20))).execute(state);
        assertEquals(new RefValue(1, new IntType()), state.getSymTable().lookup("v"));
        assertEquals(new IntValue(20), state.getHeap().get(1));
    }

    @Test
    void newWithAValueOfTheWrongTypeThrows() {
        var exp = new ValueExp(new BoolValue(true));
        assertThrows(StatementException.class, () -> new NewStmt("v", exp).execute(state));
    }

    @Test
    void newOnAnUndeclaredVariableThrows() {
        var s = new NewStmt("w", new ValueExp(new IntValue(20)));
        assertThrows(StatementException.class, () -> s.execute(state));
    }

    @Test
    void newOnAVariableThatIsNotAReferenceThrows() throws MyException {
        new VarDeclStmt("x", new IntType()).execute(state); // int x
        var s = new NewStmt("x", new ValueExp(new IntValue(20)));
        assertThrows(StatementException.class, () -> s.execute(state));
    }

    //WhileStmt
    @Test
    void whileWithANonBooleanConditionThrows() {
        var loop = new WhileStmt(new ValueExp(new IntValue(1)), new NopStmt());
        assertThrows(StatementException.class, () -> loop.execute(state));
    }

    // runs until the stack is empty
    private void runToEnd() throws MyException {
        while (!state.getExeStack().isEmpty())
            state.getExeStack().pop().execute(state);
    }

    // int c; c = <n>; while (c > 0) c = c - 1
    private IStmt countdown(int n) {
        return new CompStmt(new VarDeclStmt("c", new IntType()),
                new CompStmt(new AssignStmt("c", new ValueExp(new IntValue(n))),
                        new WhileStmt(
                                new RelationalExp(RelOp.GT, new VarExp("c"), new ValueExp(new IntValue(0))),
                                new AssignStmt("c", new ArithExp(ArithOp.SUB,
                                        new VarExp("c"), new ValueExp(new IntValue(1)))))));
    }

    @Test
    void whileRunsZeroTimes() throws MyException {
        state = PrgState.of(countdown(0));
        runToEnd();
        assertEquals(new IntValue(0), state.getSymTable().lookup("c"));
    }

    @Test
    void whileRunsOnce() throws MyException {
        state = PrgState.of(countdown(1));
        runToEnd();
        assertEquals(new IntValue(0), state.getSymTable().lookup("c"));
    }

    @Test
    void whileRunsSeveralTimes() throws MyException {
        state = PrgState.of(countdown(4));
        runToEnd();
        assertEquals(new IntValue(0), state.getSymTable().lookup("c"));
    }

}