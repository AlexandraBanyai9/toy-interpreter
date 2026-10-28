package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.adt.MyDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArithExpTest {
    private IDictionary<String, IValue> symTable;

    @BeforeEach
    void setUp() {
        symTable = new MyDictionary<>();
    }

    private static IExp num(int n) {
        return new ValueExp(new IntValue(n));
    }

    @Test
    void addAddsTwoNumbers() throws MyException {
        IExp e = new ArithExp(ArithOp.ADD, num(2), num(3));
        assertEquals(new IntValue(5), e.eval(symTable));
    }

    @Test
    void subSubtractsTwoNumbers() throws MyException {
        IExp e = new ArithExp(ArithOp.SUB, num(7), num(2));
        assertEquals(new IntValue(5), e.eval(symTable));
    }

    @Test
    void mulMultipliesTwoNumbers() throws MyException {
        IExp e = new ArithExp(ArithOp.MUL, num(3), num(5));
        assertEquals(new IntValue(15), e.eval(symTable));
    }

    @Test
    void divDividesTwoNumbers() throws MyException {
        IExp e = new ArithExp(ArithOp.DIV, num(10), num(2));
        assertEquals(new IntValue(5), e.eval(symTable));
    }

    @Test
    void divisionByZeroThrows() {
        IExp e = new ArithExp(ArithOp.DIV, num(1), num(0));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("Division by zero", ex.getMessage());
    }

    @Test
    void nonIntegerFirstOperandThrows() {
        IExp e = new ArithExp(ArithOp.ADD, new ValueExp(new BoolValue(true)), num(1));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("First operand is not an integer", ex.getMessage());
    }

    @Test
    void nonIntegerSecondOperandThrows() {
        IExp e = new ArithExp(ArithOp.ADD, num(1), new ValueExp(new BoolValue(true)));
        var ex = assertThrows(ExpressionException.class, () -> e.eval(symTable));
        assertEquals("Second operand is not an integer", ex.getMessage());
    }

    @Test
    void multiplicationInsideTreeIsEvaluatedFirst() throws MyException {
        // 2 + (3 * 5)
        IExp e = new ArithExp(ArithOp.ADD, num(2),
                new ArithExp(ArithOp.MUL, num(3), num(5)));
        assertEquals(new IntValue(17), e.eval(symTable));
    }

    @Test
    void additionInsideTreeIsEvaluatedFirst() throws MyException {
        // (2 + 3) * 5
        IExp e = new ArithExp(ArithOp.MUL,
                new ArithExp(ArithOp.ADD, num(2), num(3)), num(5));
        assertEquals(new IntValue(25), e.eval(symTable));
    }

    @Test
    void usesVariableValueFromSymTable() throws MyException {
        symTable.put("a", new IntValue(4));
        IExp e = new ArithExp(ArithOp.ADD, new VarExp("a"), num(1));
        assertEquals(new IntValue(5), e.eval(symTable));
    }
}